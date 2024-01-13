package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.*;
import com.hcmute.drink.collection.embedded.*;
import com.hcmute.drink.common.OrderItemDto;
import com.hcmute.drink.common.ToppingDto;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateOnsiteOrderRequest;
import com.hcmute.drink.dto.request.CreateShippingOrderRequest;
import com.hcmute.drink.dto.request.CreateReviewRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.*;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.model.elasticsearch.OrderIndex;
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
    private final ModelMapperUtils modelMapperUtils;
    private final SequenceService sequenceService;
    private final OrderSearchService orderSearchService;
    private final BranchService branchService;
    private final AddressService addressService;
    private final EmployeeService employeeService;

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

    public long calculateOrderBill(List<OrderItemDto> products) {
        long totalPrice = 0;
        for (OrderItemDto item : products) {
            double totalPriceToppings = 0;
            ProductCollection productInfo = productService.getById(item.getProductId().toString()); //productRepository.findById(product.getProductId().toString()).orElse(null);

            List<ToppingEmbedded> toppingList = productInfo.getToppingList() != null ? productInfo.getToppingList() : new ArrayList<>();
            List<ToppingDto> toppingDtoList = item.getToppingList() != null ? item.getToppingList() : new ArrayList<>();
            for (ToppingDto topping : toppingDtoList) {
                ToppingEmbedded toppingInfo = toppingList.stream()
                        .filter(i -> i.getName().equals(topping.getName()))
                        .findFirst()
                        .orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + topping.getName()));
                if (topping.getPrice() != toppingInfo.getPrice()) {
                    throw new CustomException(ErrorConstant.TOPPING_PRICE_INVALID);
                }

                totalPriceToppings += topping.getPrice();
            }
            SizeEmbedded sizeInfo = productInfo.getSizeList()
                    .stream()
                    .filter(it -> it.getSize().equals(item.getSize()))
                    .findFirst().orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + item.getSize()));
            if (item.getPrice() != sizeInfo.getPrice()) {
                throw new CustomException(ErrorConstant.SIZE_PRICE_INVALID);
            }
            totalPrice += (long) ((sizeInfo.getPrice() + totalPriceToppings) * item.getQuantity());
        }
        return totalPrice;
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
                    .invoiceCode(paymentData.get(VNP_TXN_REF_KEY).toString())
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

        // Sắp xếp danh sách theo khoảng cách
        allBranches.sort(Comparator.comparingDouble(branch ->
                CalculateUtils.calculateDistance(address.getLatitude(), address.getLongitude(), branch.getLatitude(), branch.getLongitude())));

        // Trả về số lượng chi nhánh cần thiết
        return allBranches.subList(0, Math.min(1, allBranches.size())).get(0);

    }
    // SERVICES =================================================================

    @Override
    @Transactional
    public CreateShippingOrderResponse createShippingOrder(CreateShippingOrderRequest body, HttpServletRequest request) {
        PaymentType paymentType = body.getPaymentType();
        OrderCollection data = modelMapperUtils.mapClass(body, OrderCollection.class);
        userService.getById(data.getUserId().toString());

        long totalPrice = calculateOrderBill(body.getItemList());
        data.setTotal(totalPrice);

        Map<String, Object> transactionMap = buildTransaction(paymentType, request, totalPrice);
        TransactionCollection transData = (TransactionCollection) transactionMap.get("transaction");

        TransactionCollection transSaved = transactionService.saveTransaction(transData);

        data.setOrderType(OrderType.SHIPPING);
        data.setTransactionId(new ObjectId(transSaved.getId()));
        String makerId = SecurityUtils.getCurrentUserId();
        OrderEventEmbedded log = OrderEventEmbedded.builder()
                .orderStatus(OrderStatus.CREATED)
                .description("You have successfully created an order")
                .isEmployee(false)
                .makerId(new ObjectId(makerId))
                .time(new Date()).build();
        data.setEventList(new ArrayList<>(Arrays.<OrderEventEmbedded>asList(log)));
        data.setCode(sequenceService.generateCode(OrderCollection.SEQUENCE_NAME, OrderCollection.PREFIX_CODE_SHIPPING, OrderCollection.LENGTH_NUMBER));

        AddressCollection addressDelivering = addressService.getAddressById(body.getAddressId());
        RecipientInfoEmbedded recipientInfo = modelMapperUtils.mapClass(addressDelivering, RecipientInfoEmbedded.class);

        BranchCollection branch = getNearestBranches(addressDelivering);
        BranchEmbedded branchEmbedded = BranchEmbedded.builder()
                .id(new ObjectId(branch.getId()))
                .address(branch.getFullAddress())
                .latitude(branch.getLatitude())
                .longitude(branch.getLongitude())
                .build();
        data.setBranch(branchEmbedded);
        data.setRecipientInfo(recipientInfo);

        OrderCollection order = orderRepository.save(data);

        CreateShippingOrderResponse resData = CreateShippingOrderResponse.builder()
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
    public CreateShippingOrderResponse createOnsiteOrder(CreateOnsiteOrderRequest body, HttpServletRequest request) {
        PaymentType paymentType = body.getPaymentType();
        OrderCollection data = modelMapperUtils.mapClass(body, OrderCollection.class);
        userService.getById(data.getUserId().toString());

        long totalPrice = calculateOrderBill(body.getItemList());
        data.setTotal(totalPrice);

        Map<String, Object> transactionMap = buildTransaction(paymentType, request, totalPrice);
        TransactionCollection transData = (TransactionCollection) transactionMap.get("transaction");
        TransactionCollection transSaved = transactionService.saveTransaction(transData);

        data.setOrderType(OrderType.ONSITE);
        data.setTransactionId(new ObjectId(transSaved.getId()));

        String makerId = SecurityUtils.getCurrentUserId();
        OrderEventEmbedded log = OrderEventEmbedded.builder()
                .orderStatus(OrderStatus.CREATED)
                .description("You have successfully created an order")
                .isEmployee(false)
                .makerId(new ObjectId(makerId))
                .time(new Date()).build();
        data.setEventList(new ArrayList<>(Arrays.<OrderEventEmbedded>asList(log)));
        data.setCode(sequenceService.generateCode(OrderCollection.SEQUENCE_NAME, OrderCollection.PREFIX_CODE_ONSITE, OrderCollection.LENGTH_NUMBER));


        BranchCollection branch = branchService.getBranchById(body.getBranchId().toString());

        BranchEmbedded branchEmbedded = BranchEmbedded.builder()
                .id(new ObjectId(branch.getId()))
                .address(branch.getFullAddress())
                .latitude(branch.getLatitude())
                .longitude(branch.getLongitude())
                .build();

        data.setBranch(branchEmbedded);
        UserCollection user = userService.getById(makerId);
        RecipientInfoEmbedded recipientInfo = RecipientInfoEmbedded.builder()
                .recipientName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .build();
        data.setRecipientInfo(recipientInfo);
        OrderCollection order = orderRepository.save(data);

        CreateShippingOrderResponse resData = CreateShippingOrderResponse.builder()
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

        List<GetShippingOrderQueueResponse> data = orderRepository.getShippingOrderQueueToday(branchId, DateUtils.createBeginingOfDate(), DateUtils.createEndOfDate(), skip, limit, orderStatus);
        return data;
    }

    @Override
    public List<GetOnsiteOrderQueueResponse> getOnsiteOrderQueueToday(OrderStatus orderStatus, int page, int size) {
        String employeeId = SecurityUtils.getCurrentUserId();

        EmployeeCollection employeeCollection = employeeService.getById(employeeId);
        String branchId = employeeCollection.getBranchId().toString();

        int skip = (page - 1) * size;
        int limit = size;
        List<GetOnsiteOrderQueueResponse> data = orderRepository.getOnsiteOrderQueueToday(branchId, DateUtils.createBeginingOfDate(), DateUtils.createEndOfDate(), skip, limit, orderStatus);

        return data;
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
        ReviewEmbedded data = modelMapperUtils.mapClass(body, ReviewEmbedded.class);
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
            resData.add(modelMapperUtils.mapClass(log, GetOrderStatusLineResponse.class));
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
        return modelMapperUtils.mapList(orderList, GetOrderHistoryForEmployeeResponse.class);
    }

    public List<GetAllVisibleProductResponse> getTopProductQuantityOrder(int top) {
        return orderRepository.getTopProductQuantityOrder(top);
    }

    public void cancelOrderPreviousDay(OrderStatus orderStatus) {
        Date startDatePrev = DateUtils.createDateTimeByToday(0, 0, 0, 0, -1);
        Date endDatePrev = DateUtils.createDateTimeByToday(23, 59, 59, 999, 0);
        List<OrderCollection> orderList = orderRepository.getShippingOrdersByStatusLastAndDateTimeCreated(startDatePrev, endDatePrev, orderStatus);
        Iterator<OrderCollection> itr = orderList.iterator();

        while (itr.hasNext()) {
            OrderCollection order = itr.next();
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
