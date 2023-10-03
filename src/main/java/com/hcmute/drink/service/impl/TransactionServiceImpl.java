package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.payment.VNPayUtils;
import com.hcmute.drink.repository.TransactionRepository;
import com.hcmute.drink.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl {
    private final TransactionRepository transactionRepository;
    private final OrderServiceImpl orderService;
    private final VNPayUtils vnPayUtils;

    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;

    public TransactionCollection createTransaction(TransactionCollection data) throws Exception {
        TransactionCollection transaction = transactionRepository.save(data);
        if(transaction == null) {
            throw new Exception(ErrorConstant.CREATED_FAILED);
        }
        return transaction;
    }

    public TransactionCollection updateTransaction(TransactionCollection data, String timeCode, HttpServletRequest request) throws Exception {
        TransactionCollection transaction = transactionRepository.findById(data.getId()).orElse(null);
        if(transaction == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + data.getId());
        }
        OrderCollection order = orderService.getOrderInfoByTransId(transaction.getId());
        // kiểm tra trạng thái transaction từ vnpay


        // Goi den VNPay de lay thong tin
        Map<String, Object> transInfo = vnPayUtils.getTransactionInfo(data.getInvoiceCode(), timeCode, request);

        modelMapperNotNull.map(data, transaction);
        // nếu giao dịch vnpay thành công
        if(transInfo.get("vnp_ResponseCode").equals("00")) {
            // nếu số tiền vnpay nhận được = total của order
            if(transInfo.get("vnp_Amount").equals(String.valueOf(order.getTotal()))) {
                transaction.setInvoiceCode(transInfo.get("vnp_TxnRef").toString());
                transaction.setStatus(PaymentStatus.PAID);
            } else {
                orderService.updateOrderEvent(order.getId(), OrderStatus.CANCELED, "You have not completed the full payment amount");
                transaction.setStatus(PaymentStatus.UNPAID);
            }
        }
        else {
            transaction.setStatus(PaymentStatus.UNPAID);
        }
        // Cập nhật kết quả vào vnpay
        TransactionCollection updatedTransaction = transactionRepository.save(transaction);
        if(updatedTransaction == null) {
            throw new Exception(ErrorConstant.UPDATE_FAILED);
        }
        return updatedTransaction;
    }
//
//    public TransactionCollection updateTransaction(TransactionCollection data) throws Exception {
//        UserCollection buyer = userRepository.findById(data.getUserId().toString()).orElse(null);
//        if(buyer == null) {
//            throw new Exception(ErrorConstant.USER_NOT_FOUND);
//        }
//        TransactionCollection oldData = transactionRepository.findById(data.getId()).orElse(null);
//        if(oldData == null) {
//            throw new Exception(ErrorConstant.NOT_FOUND);
//        }
//        modelMapperNotNull.map(data, oldData);
//        TransactionCollection newData = transactionRepository.save(oldData);
//        if(newData != null) {
//            return newData;
//        }
//        throw new Exception(ErrorConstant.UPDATE_FAILED);
//    }
}
