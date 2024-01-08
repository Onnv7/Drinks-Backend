package com.hcmute.drink.service;

import com.hcmute.drink.dto.request.CreateProductRequest;
import com.hcmute.drink.dto.request.UpdateProductRequest;
import com.hcmute.drink.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    void createProduct(CreateProductRequest body, MultipartFile image);
    GetProductByIdResponse getProductDetailsById(String id);
    GetProductEnabledByIdResponse getProductEnabledById(String id);
    List<GetProductsByCategoryIdResponse> getProductsByCategoryId(String categoryId);
    List<GetAllProductsEnabledResponse> getAllProductsEnabled(int page, int size);
    List<GetAllProductsEnabledResponse> searchProductByNameOrDescription(String key, int page, int size);
    List<GetAllProductsResponse> getAllProducts();
    void deleteProductById(String id);
    void updateProductById(UpdateProductRequest data, String id);
    List<GetAllProductsEnabledResponse> getTopProductQuantityOrder(int quantity);
}
