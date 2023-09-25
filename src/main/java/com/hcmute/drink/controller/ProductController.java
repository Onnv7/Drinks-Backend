package com.hcmute.drink.controller;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.CreateProductRequest;
import com.hcmute.drink.dto.UpdateProductRequest;
import com.hcmute.drink.model.ApiResponse;
import com.hcmute.drink.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ModelMapper modelMapper;
    private final ProductServiceImpl productService;

    @PostMapping()
    public ResponseEntity<ApiResponse> createProduct(@ModelAttribute @Validated CreateProductRequest body) {
        try {
            ProductCollection product = new ProductCollection();
            modelMapper.map(body, product);

            product = productService.createProduct(product, body.getImages());
            ApiResponse res = ApiResponse.builder()
                    .message(SuccessConstant.CREATED)
                    .timestamp(new Date())
                    .data(product)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{productId}")
    protected ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") String id) {
        try {
            ProductCollection product = productService.getProductById(id);

            ApiResponse res = ApiResponse.builder()
                    .message(SuccessConstant.GET)
                    .timestamp(new Date())
                    .data(product)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping()
    protected ResponseEntity<ApiResponse> getAllProducts() {
        try {
            List<ProductCollection> products = productService.getAllProducts();

            ApiResponse res = ApiResponse.builder()
                    .message(SuccessConstant.GET)
                    .timestamp(new Date())
                    .data(products)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{productId}")
    protected ResponseEntity<ApiResponse> deleteProductById(@PathVariable("productId") String id) {
        try {
            productService.deleteProductById(id);

            ApiResponse res = ApiResponse.builder()
                    .message(SuccessConstant.DELETED)
                    .timestamp(new Date())
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProductById(@PathVariable("productId") String id, @ModelAttribute UpdateProductRequest body) {
        try {
            ProductCollection data = modelMapper.map(body, ProductCollection.class);
            data.setId(id);
            ProductCollection resData = productService.updateProductById( data, body.getImages());

            ApiResponse res = ApiResponse.builder()
                    .message(SuccessConstant.UPDATED)
                    .timestamp(new Date())
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
