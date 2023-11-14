package com.hcmute.drink.service;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.GetRevenueByTimeResponse;
import com.hcmute.drink.dto.GetRevenueCurrentDateResponse;
import com.hcmute.drink.enums.Maker;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.payment.VNPayUtils;
import com.hcmute.drink.repository.TransactionRepository;
import com.hcmute.drink.utils.MongoDbUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final OrderService orderService;
    private final VNPayUtils vnPayUtils;
    private final MongoDbUtils mongoDbUtils;

    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;

    public TransactionCollection findTransactionById(String transId) {
        return transactionRepository.findById(transId).orElseThrow();
    }

    public TransactionCollection createTransaction(TransactionCollection data) throws Exception {
        TransactionCollection transaction = transactionRepository.save(data);
        if (transaction == null) {
            throw new Exception(ErrorConstant.CREATED_FAILED);
        }
        return transaction;
    }
    public TransactionCollection updateTransactionAfterDonePaid(String id, String invoiceCode, String timeCode, HttpServletRequest request) throws Exception {
        TransactionCollection transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        OrderCollection order = orderService.exceptionIfNotExistedOrderByTransactionId(transaction.getId());
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
                orderService.addNewOrderEvent(Maker.user ,order.getId(), OrderStatus.CANCELED, "You have not completed the full payment amount");
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


    public TransactionCollection updateTransaction(String id, HttpServletRequest request) throws Exception {
        TransactionCollection transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + id);
        }
        OrderCollection order = orderService.exceptionIfNotExistedOrderByTransactionId(transaction.getId());
        // Goi den VNPay de lay thong tin
        Map<String, Object> transInfo = vnPayUtils.getTransactionInfo(transaction.getInvoiceCode(), transaction.getTimeCode(), request);

        // nếu giao dịch vnpay thành công
        if (transInfo.get("vnp_TransactionStatus").equals("00")) {
            // nếu số tiền vnpay nhận được = total của order
            if (transInfo.get("vnp_Amount").equals(String.valueOf(order.getTotal() * 100))) {
                transaction.setStatus(PaymentStatus.PAID);
                transaction.setTotalPaid(Double.parseDouble(transInfo.get("vnp_Amount").toString())/100);
            } else {
                orderService.addNewOrderEvent(Maker.user ,order.getId(), OrderStatus.CANCELED, "You have not completed the full payment amount");
                transaction.setStatus(PaymentStatus.UNPAID);
                transaction.setTotalPaid(0);
            }
        } else {
            transaction.setStatus(PaymentStatus.UNPAID);
            transaction.setTotalPaid(0);
        }
        // Cập nhật kết quả từ vnpay vào database
        TransactionCollection updatedTransaction = transactionRepository.save(transaction);
        if (updatedTransaction == null) {
            throw new Exception(ErrorConstant.UPDATE_FAILED);
        }
        return updatedTransaction;
    }

    public TransactionCollection completeTransaction(String transId) throws Exception {
        OrderCollection order = orderService.exceptionIfNotExistedOrderByTransactionId(transId);
        TransactionCollection trans = findTransactionById(transId);
        double totalPaid = order.getTotal();
        trans.setStatus(PaymentStatus.PAID);
        trans.setTotalPaid(totalPaid);
        return transactionRepository.save(trans);
    }

    public List<GetRevenueByTimeResponse> getRevenueByTime(String time) {
        return transactionRepository.getRevenueByTime(time);
    }

    public GetRevenueCurrentDateResponse getRevenueCurrentDate() {
        Date startDate = mongoDbUtils.createCurrentDateTime(0, 0, 0, 0);
        Date endDate = mongoDbUtils.createCurrentDateTime(23, 59, 59, 999);
        GetRevenueCurrentDateResponse current = transactionRepository.getRevenueCurrentDate(startDate,  endDate);

        if (current == null) {
            current = new GetRevenueCurrentDateResponse();
            current.setRevenue(0);
            current.setRatio(-100);
            return current;
        }

        Date startDatePrev = mongoDbUtils.createPreviousDay(0, 0, 0, 0, 1);
        Date endDatePrev = mongoDbUtils.createPreviousDay(23, 59, 59, 999, 1);
        GetRevenueCurrentDateResponse prev = transactionRepository.getRevenueCurrentDate(startDatePrev,  endDatePrev);

        if(prev == null) {
            prev = new GetRevenueCurrentDateResponse();
            prev.setRevenue(0);
        }
        double ratio =(current.getRevenue() - prev.getRevenue()) / (prev.getRevenue());
        current.setRatio(ratio);
        return current;
    }
}
