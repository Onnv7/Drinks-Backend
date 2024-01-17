package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.*;
import com.hcmute.drink.collection.embedded.*;
import com.hcmute.drink.dto.common.OrderItemDto;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateOnsiteOrderRequest;
import com.hcmute.drink.dto.request.CreateShippingOrderRequest;
import com.hcmute.drink.dto.request.CreateReviewRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.*;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.model.elasticsearch.OrderIndex;
import com.hcmute.drink.service.common.ModelMapperService;
import com.hcmute.drink.service.database.IOrderService;
import com.hcmute.drink.service.elasticsearch.OrderSearchService;
import com.hcmute.drink.utils.*;
import com.hcmute.drink.repository.database.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.hcmute.drink.constant.VNPayConstant.*;


@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ModelMapperService modelMapperService;
    private final SequenceService sequenceService;
    private final OrderSearchService orderSearchService;
    private final BranchService branchService;
    private final AddressService addressService;
    private final EmployeeService employeeService;
    private final CouponService couponService;
    private final UserCouponService userCouponService;

    @Autowired
    @Lazy
    private TransactionService transactionService;

    public OrderCollection getById(String transactionId) {
        return orderRepository.findByTransactionId(new ObjectId(transactionId))
                .orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + transactionId));
    }

    public OrderCollection getByOrderId(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }

    public Map<String, Object> calculateOrderBill(List<OrderItemDto> productList, List<OrderItemEmbedded> itemList) {
        long totalPrice = 0;
        List<String> couponUsedList = new ArrayList<>();
        Map<String, Object> result = new HashMap<String, Object>();
        int itemSize = productList.size();

        Boolean isMultiple = null;
        Boolean prevMultiple = null;
        Boolean currentMultiple = null;
        for (int i = 0; i < itemSize; i++) {
            OrderItemDto productDto = productList.get(i);
            OrderItemEmbedded item = itemList.get(i);
            double totalPriceToppings = 0;
            ProductCollection productInfo = productService.getById(productDto.getProductId().toString());
            if (productInfo.isCanDelete()) {
                productInfo.setCanDelete(false);
                productService.saveProduct(productInfo);
            }
            List<ToppingEmbedded> toppingList = productInfo.getToppingList() != null ? productInfo.getToppingList() : new ArrayList<>();
            List<String> toppingNameList = productDto.getToppingNameList() != null ? productDto.getToppingNameList() : new ArrayList<>();

            // TODO: vieét query tìm topping price
            if (!toppingList.isEmpty()) {
                item.setToppingList(new ArrayList<>());
            }
            for (String topping : toppingNameList) {
                ToppingEmbedded toppingInfo = toppingList.stream()
                        .filter(it -> it.getName().equals(topping))
                        .findFirst()
                        .orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + topping));
                totalPriceToppings += toppingInfo.getPrice();
                item.getToppingList().add(toppingInfo);
            }

            SizeEmbedded sizeInfo = productInfo.getSizeList()
                    .stream()
                    .filter(it -> it.getSize().equals(productDto.getSize()))
                    .findFirst().orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + productDto.getSize()));


            totalPrice += (long) ((sizeInfo.getPrice() + totalPriceToppings) * productDto.getQuantity());

            item.setPrice(sizeInfo.getPrice());

            if (productDto.getCouponProductCode() != null) {
                if(couponUsedList.contains(productDto.getCouponProductCode())) {
                    throw new CustomException(ErrorConstant.COUPON_DUPLICATED);
                }
                CouponCollection couponCollection = couponService.getAndCheckValidCoupon(productDto.getCouponProductCode());

                if ((isMultiple != null && !isMultiple)
                        || (Boolean.TRUE.equals(isMultiple) && !couponCollection.isCanMultiple())
                        || couponCollection.getCouponType() == CouponType.ORDER
                        || couponCollection.getCouponType() == CouponType.SHIPPING
                ) {
                    throw new CustomException(ErrorConstant.COUPON_INVALID);
                }
                isMultiple = couponCollection.isCanMultiple();

                if (couponCollection.getCouponType() == CouponType.PRODUCT) {
                    MoneyDiscountEmbedded discount = couponService.checkMoneyCouponProduct(couponCollection, productDto.getProductId().toString());
                    if (discount.getUnit() == DiscountUnitType.MONEY) {
                        totalPrice -= ((Number) discount.getValue()).longValue();
                        item.setMoneyDiscount(discount.getValue());
                    } else if (discount.getUnit() == DiscountUnitType.PERCENTAGE) {
                        totalPrice -= item.getPrice() * ((Number) discount.getValue()).longValue() / 100;
                        item.setMoneyDiscount(item.getPrice() * ((Number) discount.getValue()).longValue() / 100);
                    }
                } else if (couponCollection.getCouponType() == CouponType.BUY_PRODUCT_GET_PRODUCT) {
                    ProductGiftEmbedded discount = couponService.checkBuyGetCouponProduct(couponCollection, productDto.getProductId().toString());
                    item.setProductGift(discount);
                }
                couponUsedList.add(couponCollection.getCode());
            }
        }
        result.put("total", totalPrice);
        result.put("isMultiple", isMultiple);
        result.put("couponUsedList", couponUsedList);
        return result;
    }

    private Map<String, Object> buildTransaction(PaymentType paymentType, HttpServletRequest request, long totalPrice) {
        TransactionCollection transData = new TransactionCollection();
        Map<String, Object> result = new HashMap<>();
        if (paymentType == PaymentType.CASHING) {
            transData = TransactionCollection.builder()
                    .status(PaymentStatus.UNPAID)
                    .paymentType(PaymentType.CASHING).build();
        } else if (paymentType == PaymentType.BANKING_VNPAY) {
            Map<String, String> paymentData = null;
            try {
                paymentData = VNPayUtils.createUrlPayment(request, totalPrice, "Shipping Order Info");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            transData = TransactionCollection.builder()
                    .invoiceCode(paymentData.get(VNP_TXN_REF_KEY))
                    .timeCode(paymentData.get(VNP_CREATE_DATE_KEY))
                    .status(PaymentStatus.UNPAID)
                    .paymentType(paymentType)
                    .build();
            result.put(VNP_URL_KEY, paymentData.get(VNP_URL_KEY));
        }
        result.put("transaction", transData);
        return result;
    }

    public BranchCollection getNearestBranches(AddressCollection address) {
        List<BranchCollection> allBranches = branchService.getAllBranch();

        allBranches.sort(Comparator.comparingDouble(branch ->
                CalculateUtils.calculateDistance(address.getLatitude(), address.getLongitude(), branch.getLatitude(), branch.getLongitude())));

        return allBranches.subList(0, Math.min(1, allBranches.size())).get(0);

    }

    private void saveCouponUsed(List<?> couponUsedList, String userId) {
        if (couponUsedList != null && !couponUsedList.isEmpty()) {
            for (Object couponCode : couponUsedList) {
                userCouponService.saveUserCoupon(UserCouponCollection.builder()
                        .userId(new ObjectId(userId))
                        .couponCode(couponCode.toString())
                        .build());
            }
        }
    }

    private List<OrderEventEmbedded> createOrderCreateEvent(String userId) {
        OrderEventEmbedded log = OrderEventEmbedded.builder()
                .orderStatus(OrderStatus.CREATED)
                .description("You have successfully created an order")
                .isEmployee(false)
                .makerId(new ObjectId(userId))
                .time(new Date()).build();
        return new ArrayList<>(Collections.<OrderEventEmbedded>singletonList(log));
    }

    private BranchEmbedded createBranchEmbedded(String branchId) {
        BranchCollection branch = branchService.getBranchById(branchId);
        return BranchEmbedded.builder()
                .id(new ObjectId(branch.getId()))
                .address(branch.getFullAddress())
                .latitude(branch.getLatitude())
                .longitude(branch.getLongitude())
                .build();
    }
    // SERVICES =================================================================

    @Override
    @Transactional
    public CreateOrderResponse createShippingOrder(CreateShippingOrderRequest body, HttpServletRequest request) {
        long totalPrice = 0L;
        String userId = SecurityUtils.getCurrentUserId();
        OrderCollection data = modelMapperService.mapClass(body, OrderCollection.class);

        data.setOrderType(OrderType.SHIPPING);
        userService.getById(userId);
        data.setUserId(new ObjectId(userId));


        Map<String, Object> resultCalculate = calculateOrderBill(body.getItemList(), data.getItemList());
        totalPrice = (Long) resultCalculate.get("total");
        Boolean isMultiple = (Boolean) resultCalculate.get("isMultiple");
        List<?> couponUsedList = (List<?>) resultCalculate.get("couponUsedList");


        if (isMultiple != null && !isMultiple && (body.getShippingCouponCode() != null || body.getOrderCouponCode() != null)) {
            throw new CustomException(ErrorConstant.COUPON_INVALID);
        }
        if (body.getOrderCouponCode() != null) {
            CouponCollection coupon = couponService.getAndCheckValidCoupon(body.getOrderCouponCode());
            if ((isMultiple != null && !coupon.isCanMultiple()) || coupon.getCouponType() != CouponType.ORDER) {
                throw new CustomException(ErrorConstant.COUPON_INVALID);
            } else if(isMultiple == null) {
                isMultiple = coupon.isCanMultiple();
            }
            MoneyDiscountEmbedded discount = couponService.checkMoneyCouponOrderBill(coupon, totalPrice);
            long moneyDiscount = ((Number) discount.getValue()).longValue();
            totalPrice -= moneyDiscount;
            data.setOrderDiscount(moneyDiscount);
        }
        // tới đây chỉ có tể là isMultiple true  hoặc null hoặc false vì có tể vào order coupon ở trên
        if(isMultiple != null  && !isMultiple && body.getOrderCouponCode() != null) {
            throw new CustomException(ErrorConstant.COUPON_INVALID);
        }
        if (body.getShippingCouponCode() != null) {
            CouponCollection coupon = couponService.getAndCheckValidCoupon(body.getShippingCouponCode());
            if ((isMultiple != null && !coupon.isCanMultiple()) || coupon.getCouponType() != CouponType.SHIPPING) {
                throw new CustomException(ErrorConstant.COUPON_INVALID);
            }
            MoneyDiscountEmbedded discount = couponService.checkMoneyCouponOrderBill(coupon, totalPrice);
            long moneyDiscount = 0;
            if(discount.getUnit() == DiscountUnitType.MONEY) {
                moneyDiscount = ((Number) discount.getValue()).longValue();
            } else if(discount.getUnit() == DiscountUnitType.PERCENTAGE) {
                moneyDiscount = body.getShippingFee() * ((Number) discount.getValue()).longValue() / 100;
            }

            long shippingFee = body.getShippingFee() - moneyDiscount;
            totalPrice += shippingFee > 0 ? shippingFee : 0;
            data.setShippingDiscount(moneyDiscount);
        } else {
            totalPrice += body.getShippingFee();
        }

        if (totalPrice != body.getTotal()) {
            throw new CustomException(ErrorConstant.TOTAL_ORDER_INVALID);
        }
        data.setTotal(totalPrice);

        Map<String, Object> transactionMap = buildTransaction(body.getPaymentType(), request, totalPrice);
        TransactionCollection transData = (TransactionCollection) transactionMap.get("transaction");
        TransactionCollection transSaved = transactionService.saveTransaction(transData);
        data.setTransactionId(new ObjectId(transSaved.getId()));


        data.setEventList(createOrderCreateEvent(userId));
        data.setCode(sequenceService.generateCode(OrderCollection.SEQUENCE_NAME, OrderCollection.PREFIX_CODE_SHIPPING, OrderCollection.LENGTH_NUMBER));

        AddressCollection addressDelivering = addressService.getAddressById(body.getAddressId());
        RecipientInfoEmbedded recipientInfo = modelMapperService.mapClass(addressDelivering, RecipientInfoEmbedded.class);
        data.setRecipientInfo(recipientInfo);

        BranchCollection branch = getNearestBranches(addressDelivering);
        BranchEmbedded branchEmbedded = createBranchEmbedded(branch.getId());
        data.setBranch(branchEmbedded);
        saveCouponUsed( couponUsedList, userId);

        OrderCollection order = orderRepository.save(data);

        CreateOrderResponse resData = CreateOrderResponse.builder()
                .orderId(order.getId())
                .transactionId(transSaved.getId())
                .build();
        if (transactionMap.get(VNP_URL_KEY) != null) {
            resData.setPaymentUrl(transactionMap.get(VNP_URL_KEY).toString());
        }
        orderSearchService.createOrder(order);
        return resData;
    }

    @Override
    public CreateOrderResponse createOnsiteOrder(CreateOnsiteOrderRequest body, HttpServletRequest request) {
        long totalPrice = 0;
        String userId = SecurityUtils.getCurrentUserId();

        PaymentType paymentType = body.getPaymentType();
        OrderCollection data = modelMapperService.mapClass(body, OrderCollection.class);
        data.setOrderType(OrderType.ONSITE);

        UserCollection user = userService.getById(userId);
        data.setUserId(new ObjectId(userId));

        Map<String, Object> resultCalculate = calculateOrderBill(body.getItemList(), data.getItemList());
        totalPrice = (Long) resultCalculate.get("total");
        Boolean isMultiple = (Boolean) resultCalculate.get("isMultiple");
        List<?> couponUsedList = (List<?>) resultCalculate.get("couponUsedList");

        if (isMultiple != null && !isMultiple && body.getOrderCouponCode() != null) {
            throw new CustomException(ErrorConstant.COUPON_INVALID);
        }
        if (body.getOrderCouponCode() != null) {
            CouponCollection coupon = couponService.getAndCheckValidCoupon(body.getOrderCouponCode());
            if ((isMultiple != null && !coupon.isCanMultiple()) || coupon.getCouponType() != CouponType.ORDER) {
                throw new CustomException(ErrorConstant.COUPON_INVALID);
            }
            MoneyDiscountEmbedded discount = couponService.checkMoneyCouponOrderBill(coupon, totalPrice);
            long moneyDiscount = ((Number) discount.getValue()).longValue();
            totalPrice -= moneyDiscount;
            data.setOrderDiscount(moneyDiscount);
        }
        if (totalPrice != body.getTotal()) {
            throw new CustomException(ErrorConstant.TOTAL_ORDER_INVALID);
        }
        data.setTotal(totalPrice);

        Map<String, Object> transactionMap = buildTransaction(paymentType, request, totalPrice);
        TransactionCollection transData = (TransactionCollection) transactionMap.get("transaction");
        TransactionCollection transSaved = transactionService.saveTransaction(transData);

        data.setTransactionId(new ObjectId(transSaved.getId()));

        data.setEventList(createOrderCreateEvent(userId));
        data.setCode(sequenceService.generateCode(OrderCollection.SEQUENCE_NAME, OrderCollection.PREFIX_CODE_ONSITE, OrderCollection.LENGTH_NUMBER));

        BranchEmbedded branchEmbedded = createBranchEmbedded(body.getBranchId().toString());
        data.setBranch(branchEmbedded);

        RecipientInfoEmbedded recipientInfo = RecipientInfoEmbedded.builder()
                .recipientName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .build();
        data.setRecipientInfo(recipientInfo);

        OrderCollection order = orderRepository.save(data);

        CreateOrderResponse resData = CreateOrderResponse.builder()
                .orderId(order.getId())
                .transactionId(transSaved.getId())
                .build();
        if (transactionMap.get(VNP_URL_KEY) != null) {
            resData.setPaymentUrl(transactionMap.get(VNP_URL_KEY).toString());
        }

        saveCouponUsed( couponUsedList, userId);
        orderSearchService.createOrder(order);
        return resData;
    }

    @Override
    public void addNewOrderEvent(Maker maker, String id, OrderStatus orderStatus, String description) {
        OrderCollection order = getByOrderId(id);
        String employeeId = SecurityUtils.getCurrentUserId();
        order.getEventList().add(OrderEventEmbedded.builder()
                .orderStatus(orderStatus)
                .description(description)
                .isEmployee(maker.equals(Maker.employee))
                .makerId(new ObjectId(employeeId))
                .time(new Date())
                .build()
        );

        OrderCollection updatedOrder = orderRepository.save(order);
        orderSearchService.upsertOrder(updatedOrder);
    }


    @Override
    public List<GetShippingOrderQueueResponse> getShippingOrderQueueToday(OrderStatus orderStatus, int page, int size) {
        String employeeId = SecurityUtils.getCurrentUserId();

        EmployeeCollection employeeCollection = employeeService.getById(employeeId);
        String branchId = employeeCollection.getBranchId().toString();

        int skip = (page - 1) * size;
        int limit = size;

        return orderRepository.getShippingOrderQueueToday(branchId, DateUtils.createBeginingOfDate(), DateUtils.createEndOfDate(), skip, limit, orderStatus);
    }

    @Override
    public List<GetOnsiteOrderQueueResponse> getOnsiteOrderQueueToday(OrderStatus orderStatus, int page, int size) {
        String employeeId = SecurityUtils.getCurrentUserId();

        EmployeeCollection employeeCollection = employeeService.getById(employeeId);
        String branchId = employeeCollection.getBranchId().toString();

        int skip = (page - 1) * size;
        int limit = size;

        return orderRepository.getOnsiteOrderQueueToday(branchId, DateUtils.createBeginingOfDate(), DateUtils.createEndOfDate(), skip, limit, orderStatus);
    }

    @Override
    public GetOrderDetailsResponse getOrderDetailsById(String id) {
        GetOrderDetailsResponse order = orderRepository.getOrderDetailsById(id);
        if (order == null) {
            throw new CustomException(ErrorConstant.NOT_FOUND + id);
        }
        return order;
    }

    @Override
    public List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserId(String userId, OrderStatus orderStatus, int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        List<GetAllOrderHistoryByUserIdResponse> order = orderRepository.getOrdersHistoryByUserIdAndOrderStatus(new ObjectId(userId), orderStatus, skip, limit);
        if (order == null) {
            throw new CustomException(ErrorConstant.NOT_FOUND + userId);
        }
        return order;
    }


    @Override
    public void createReviewForOrder(CreateReviewRequest body, String id) {
        ReviewEmbedded data = modelMapperService.mapClass(body, ReviewEmbedded.class);
        OrderCollection order = getByOrderId(id);
        if (order.getEventList().get(order.getEventList().size() - 1).getOrderStatus() != OrderStatus.SUCCEED) {
            throw new CustomException(ErrorConstant.ORDER_NOT_COMPLETED);
        }
        order.setReview(data);
        orderRepository.save(order);
    }

    @Override
    public List<GetOrderStatusLineResponse> getOrderEventLogById(String orderId) {
        OrderCollection order = orderRepository.findById(orderId).orElseThrow();
        List<GetOrderStatusLineResponse> resData = new ArrayList<GetOrderStatusLineResponse>();
        List<OrderEventEmbedded> logs = order.getEventList();
        for (OrderEventEmbedded log : logs) {
            resData.add(modelMapperService.mapClass(log, GetOrderStatusLineResponse.class));
        }
        return resData;
    }

    @Override
    public List<GetOrderHistoryForEmployeeResponse> getOrderHistoryPageForEmployee(OrderStatus orderStatus, int page, int size) {
        int skip = (page - 1) * size;
        int limit = size;
        return orderRepository.getAllOrderHistoryForEmployee(orderStatus, skip, limit);
    }

    @Override
    public GetOrderQuantityByStatusResponse getOrderQuantityByStatusAtCurrentDate(OrderStatus orderStatus) {
        Date startDate = DateUtils.createDateTimeByToday(0, 0, 0, 0, 0);
        Date endDate = DateUtils.createDateTimeByToday(23, 59, 59, 999, 0);
        GetOrderQuantityByStatusResponse current = orderRepository.getOrderQuantityByStatus(orderStatus, startDate, endDate);
        if (current == null) {
            current = new GetOrderQuantityByStatusResponse();
            current.setOrderQuantity(0);
        }

        Date startDatePrev = DateUtils.createDateTimeByToday(0, 0, 0, 0, -1);
        Date endDatePrev = DateUtils.createDateTimeByToday(23, 59, 59, 999, -1);
        GetOrderQuantityByStatusResponse prev = orderRepository.getOrderQuantityByStatus(orderStatus, startDatePrev, endDatePrev);
        int difference = 0;
        if (prev != null) {
            difference = current.getOrderQuantity() - prev.getOrderQuantity();
        } else {
            difference = current.getOrderQuantity();
        }
        current.setDifference(difference);
        return current;
    }

    @Override
    public List<GetOrderHistoryForEmployeeResponse> searchOrderHistoryForEmployee(OrderStatus orderStatus, String key, int page, int size) {
        List<OrderIndex> orderList = orderSearchService.searchOrder(key, orderStatus, page, size);
        return modelMapperService.mapList(orderList, GetOrderHistoryForEmployeeResponse.class);
    }

    public List<GetAllVisibleProductResponse> getTopProductQuantityOrder(int top) {
        return orderRepository.getTopProductQuantityOrder(top);
    }

    public void cancelOrderPreviousDay(OrderStatus orderStatus) {
        Date startDatePrev = DateUtils.createDateTimeByToday(0, 0, 0, 0, -1);
        Date endDatePrev = DateUtils.createDateTimeByToday(23, 59, 59, 999, 0);
        List<OrderCollection> orderList = orderRepository.getShippingOrdersByStatusLastAndDateTimeCreated(startDatePrev, endDatePrev, orderStatus);

        for (OrderCollection order : orderList) {
            List<OrderEventEmbedded> orderStatusList = order.getEventList();
            orderStatusList.add(OrderEventEmbedded.builder()
                    .orderStatus(OrderStatus.CANCELED)
                    .description("Order automatically canceled")
                    .time(new Date())
                    .build());
            order.setEventList(orderStatusList);
            orderRepository.save(order);
        }
    }
}
