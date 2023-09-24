package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.CreateProductRequest;
import com.hcmute.drink.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl {
    private final ProductRepository productRepository;

    public boolean createProduct(ProductCollection data) throws Exception {
        ProductCollection product = productRepository.save(data);
        if (product != null) {
            return true;
        }
        throw new Exception(ErrorConstant.CREATED_FAILED);
    }
}
