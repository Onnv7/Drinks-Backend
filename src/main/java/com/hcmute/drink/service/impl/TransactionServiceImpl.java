package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.repository.TransactionRepository;
import com.hcmute.drink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;

    public TransactionCollection createTransaction(TransactionCollection data) throws Exception {
        UserCollection buyer = userRepository.findById(data.getUserId().toString()).orElse(null);
        if(buyer == null) {
            throw new Exception(ErrorConstant.USER_NOT_FOUND);
        }
        TransactionCollection transaction = transactionRepository.save(data);
        if(transaction == null) {
            throw new Exception(ErrorConstant.CREATED_FAILED);
        }
        return transaction;
    }

    public TransactionCollection updateTransaction(TransactionCollection data) throws Exception {
        UserCollection buyer = userRepository.findById(data.getUserId().toString()).orElse(null);
        if(buyer == null) {
            throw new Exception(ErrorConstant.USER_NOT_FOUND);
        }
        TransactionCollection oldData = transactionRepository.findById(data.getId()).orElse(null);
        if(oldData == null) {
            throw new Exception(ErrorConstant.NOT_FOUND);
        }
        modelMapperNotNull.map(data, oldData);
        TransactionCollection newData = transactionRepository.save(oldData);
        if(newData != null) {
            return newData;
        }
        throw new Exception(ErrorConstant.UPDATE_FAILED);
    }
}
