package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.CreateProductRequest;
import com.hcmute.drink.dto.request.UpdateProductRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.ProductStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    void createProduct(CreateProductRequest body, MultipartFile image);
    GetProductByIdResponse getProductDetailsById(String id);
    GetProductEnabledByIdResponse getProductEnabledById(String id);
    List<GetProductsByCategoryIdResponse> getProductsByCategoryId(String categoryId);
    List<GetAllVisibleProductResponse> getAllProductsVisible(int page, int size);
    List<GetAllVisibleProductResponse> searchProductVisible(String key, int page, int size);
    GetProductListResponse getProductList(String key, int page, int size, String categoryId, ProductStatus productStatus);
    GetProductListResponse searchProduct(String key, int page, int size, String categoryId, String productStatus);
    void deleteProductById(String id);
    DeleteSomeProductResponse deleteSomeProductById(List<String> productIdList);
    void updateProductById(UpdateProductRequest body, String id);
    List<GetAllVisibleProductResponse> getTopProductQuantityOrder(int quantity);
}
