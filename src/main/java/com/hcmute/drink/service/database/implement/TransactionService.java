package com.hcmute.drink.service.implement;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.response.GetRevenueByTimeResponse;
import com.hcmute.drink.dto.response.GetRevenueCurrentDateResponse;
import com.hcmute.drink.enums.Maker;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.service.ITransactionService;
import com.hcmute.drink.utils.VNPayUtils;
import com.hcmute.drink.repository.TransactionRepository;
import com.hcmute.drink.utils.MongoDbUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final OrderService orderService;
    private final VNPayUtils vnPayUtils;
    private final MongoDbUtils mongoDbUtils;

    public TransactionCollection getById(String transId) {
        return transactionRepository.findById(transId).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + transId));
    }

    public TransactionCollection saveTransaction(TransactionCollection data) {
        return transactionRepository.save(data);
    }

    public TransactionCollection updateTransactionAfterDonePaid(String id, String invoiceCode, String timeCode, HttpServletRequest request) throws Exception {
        TransactionCollection transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        OrderCollection order = orderService.getById(transaction.getId());
        // kiểm tra trạng thái transaction từ vnpay

        // Goi den VNPay de lay thong tin
        Map<String, Object> transInfo = vnPayUtils.getTransactionInfo(invoiceCode, timeCode, request);

        // nếu giao dịch vnpay thành công
        transaction.setInvoiceCode(transInfo.get("vnp_TxnRef").toString());
        transaction.setTimeCode(timeCode);
        transaction.setStatus(PaymentStatus.UNPAID);
        if (transInfo.get("vnp_ResponseCode").equals("00")) {
            // nếu số tiền vnpay nhận được = total của order
            if (transInfo.get("vnp_Amount").equals(String.valueOf(order.getTotal()))) {
                transaction.setInvoiceCode(transInfo.get("vnp_TxnRef").toString());
                transaction.setTimeCode(transInfo.get("vnp_PayDate").toString());
                transaction.setStatus(PaymentStatus.PAID);
            } else {
                orderService.addNewOrderEvent(Maker.user, order.getId(), OrderStatus.CANCELED, "You have not completed the full payment amount");
                transaction.setTimeCode(transInfo.get("vnp_PayDate").toString());
                transaction.setStatus(PaymentStatus.UNPAID);
            }
        } else {
            transaction.setStatus(PaymentStatus.UNPAID);
        }
        // Cập nhật kết quả vào vnpay
        TransactionCollection updatedTransaction = transactionRepository.save(transaction);
        if (updatedTransaction == null) {
            throw new Exception(ErrorConstant.UPDATE_FAILED);
        }
        return updatedTransaction;
    }

    @Override
    public void updateTransaction(String id, HttpServletRequest request) {
        TransactionCollection transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            throw new CustomException(ErrorConstant.NOT_FOUND + id);
        }
        OrderCollection order = orderService.getById(transaction.getId());
        // Goi den VNPay de lay thong tin
        Map<String, Object> transInfo = null;
        try {
            transInfo = vnPayUtils.getTransactionInfo(transaction.getInvoiceCode(), transaction.getTimeCode(), request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // nếu giao dịch vnpay thành công
        if (transInfo.get("vnp_TransactionStatus").equals("00") && transInfo.get("vnp_Amount").equals(String.valueOf(order.getTotal() * 100))) {
            // nếu số tiền vnpay nhận được = total của order
//            if () {
            transaction.setStatus(PaymentStatus.PAID);
            transaction.setTotalPaid(Double.parseDouble(transInfo.get("vnp_Amount").toString()) / 100);
//            } else {
//                orderService.addNewOrderEvent(Maker.user ,order.getId(), OrderStatus.CANCELED, "You have not completed the full payment amount");
//                transaction.setStatus(PaymentStatus.UNPAID);
//                transaction.setTotalPaid(0);
//            }
        } else {
            orderService.addNewOrderEvent(Maker.user, order.getId(), OrderStatus.CANCELED, "You have not completed the full payment amount");
            transaction.setStatus(PaymentStatus.UNPAID);
            transaction.setTotalPaid(0);
        }
        // Cập nhật kết quả từ vnpay vào database
        transactionRepository.save(transaction);
    }

    @Override
    public void completeTransaction(String transId)  {
        OrderCollection order = orderService.getById(transId);
        TransactionCollection trans = getById(transId);
        double totalPaid = order.getTotal();
        trans.setStatus(PaymentStatus.PAID);
        trans.setTotalPaid(totalPaid);
        transactionRepository.save(trans);
    }

    @Override
    public List<GetRevenueByTimeResponse> getRevenueByTime(String time) {
        return transactionRepository.getRevenueByTime(time);
    }

    @Override
    public GetRevenueCurrentDateResponse getRevenueCurrentDate() {
        Date startDate = mongoDbUtils.createCurrentDateTime(0, 0, 0, 0);
        Date endDate = mongoDbUtils.createCurrentDateTime(23, 59, 59, 999);
        GetRevenueCurrentDateResponse current = transactionRepository.getRevenueCurrentDate(startDate, endDate);

        if (current == null) {
            current = new GetRevenueCurrentDateResponse();
            current.setRevenue(0);
            current.setRatio(-100);
            return current;
        }

        Date startDatePrev = mongoDbUtils.createPreviousDay(0, 0, 0, 0, 1);
        Date endDatePrev = mongoDbUtils.createPreviousDay(23, 59, 59, 999, 1);
        GetRevenueCurrentDateResponse prev = transactionRepository.getRevenueCurrentDate(startDatePrev, endDatePrev);

        if (prev == null) {
            prev = new GetRevenueCurrentDateResponse();
            prev.setRevenue(0);
        }
        double ratio = (current.getRevenue() - prev.getRevenue()) / (prev.getRevenue()) * 100;
        current.setRatio(ratio);
        return current;
    }
}
