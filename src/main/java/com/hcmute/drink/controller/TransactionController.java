package com.hcmute.drink.controller;

import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.CreateTransactionRequest;
import com.hcmute.drink.dto.UpdateTransactionRequest;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.impl.TransactionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hcmute.drink.constant.SecurityConstant.SET_ADMIN_ROLE;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = TRANSACTION_CONTROLLER_TITLE)
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionServiceImpl transactionService;
    private final ModelMapper modelMapper;
    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;

//    @Operation(summary = TRANSACTION_CREATE_SUM, description = TRANSACTION_CREATE_DES)
//    @ApiResponse(responseCode = StatusCode.CODE_CREATED, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
//    @PostMapping()
//    public ResponseEntity<ResponseAPI> createTransaction(@RequestBody @Validated CreateTransactionRequest body) {
//        try {
//            TransactionCollection data = modelMapper.map(body, TransactionCollection.class);
//            TransactionCollection savedData = transactionService.createTransaction(data);
//
//            ResponseAPI res = ResponseAPI.builder()
//                    .message(SuccessConstant.CREATED)
//                    .data(savedData)
//                    .build();
//            return new ResponseEntity<>(res, StatusCode.CREATED);
//        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    @Operation(summary = TRANSACTION_UPDATE_BY_ID_SUM, description = TRANSACTION_UPDATE_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.UPDATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PatchMapping("/{transId}")
    // sau khi thanh toán (thành công/thất bại) => update transaction
    public ResponseEntity<ResponseAPI> updateTransaction(@PathVariable("transId") String id, @RequestBody @Validated UpdateTransactionRequest body, HttpServletRequest request) {
        try {
            TransactionCollection data =  TransactionCollection.builder()
                    .id(id)
                    .build();
            modelMapper.map(body, data);
            TransactionCollection newData =  transactionService.updateTransaction(data, body.getTransactionTimeCode(), request);

            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.UPDATED)
                    .data(newData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
