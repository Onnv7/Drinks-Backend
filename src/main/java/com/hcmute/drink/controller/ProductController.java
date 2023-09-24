package com.hcmute.drink.controller;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.CreateProductRequest;
import com.hcmute.drink.model.ApiResponse;
import com.hcmute.drink.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ModelMapper modelMapper;
    private final ProductServiceImpl productService;

    @PostMapping()
    public ResponseEntity<ApiResponse> createProduct(@RequestBody CreateProductRequest body) {
        try {
            ProductCollection product = modelMapper.map(body, ProductCollection.class);
            productService.createProduct(product);
            ApiResponse res = ApiResponse.builder()
                    .message(SuccessConstant.CREATED)
                    .timestamp(new Date())
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
