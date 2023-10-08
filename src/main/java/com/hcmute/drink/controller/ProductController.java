package com.hcmute.drink.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = PRODUCT_CONTROLLER_TITLE)
@RestController
@RequestMapping(PRODUCT_BASE_PATH)
@RequiredArgsConstructor
public class ProductController {
    private final ModelMapper modelMapper;
    private final ProductServiceImpl productService;


    @Operation(summary = PRODUCT_CREATE_SUM, description = PRODUCT_CREATE_DES)
    @ApiResponse(responseCode = StatusCode.CODE_CREATED, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = PRODUCT_CREATE_SUB_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAPI> createProduct(@ModelAttribute @Validated CreateProductRequest body) {
        try {
            ProductCollection product = modelMapper.map(body, ProductCollection.class);

            product = productService.createProduct(product, body.getImageList());
            CreateProductResponse resData = modelMapper.map(product, CreateProductResponse.class);

            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.CREATED)
                    .timestamp(new Date())
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = PRODUCT_GET_BY_ID_SUM, description = PRODUCT_GET_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = PRODUCT_GET_BY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getProductById(@PathVariable("productId") String id) {
        try {
            ProductCollection product = productService.findProductById(id);
            GetProductByIdResponse resData = modelMapper.map(product, GetProductByIdResponse.class);

            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.GET)
                    .timestamp(new Date())
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = PRODUCT_GET_BY_CATEGORY_ID_SUM, description = PRODUCT_GET_BY_CATEGORY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = PRODUCT_GET_BY_CATEGORY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getProductByCategoryId(@PathVariable("categoryId") String categoryId) {
        try {
            List<GetProductsByCategoryIdResponse> products = productService.findProductsByCategoryId(categoryId);


            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.GET)
                    .timestamp(new Date())
                    .data(products)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = PRODUCT_GET_ALL_SUM, description = PRODUCT_GET_ALL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = PRODUCT_GET_ALL_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getAllProducts() {
        try {
            List<ProductCollection> products = productService.findAllProducts();
            List<GetAllProductsResponse> resData = new ArrayList<>();
            for (ProductCollection product : products) {
                GetAllProductsResponse response = new GetAllProductsResponse();
                modelMapper.map(product, response);
                resData.add(response);
            }

            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.GET)
                    .timestamp(new Date())
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = PRODUCT_DELETE_BY_ID_SUM, description = PRODUCT_DELETE_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.DELETED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @DeleteMapping(path = PRODUCT_DELETE_BY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> deleteProductById(@PathVariable("productId") String id) {
        try {
            productService.deleteProductById(id);

            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.DELETED)
                    .timestamp(new Date())
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = PRODUCT_UPDATE_BY_ID_SUM, description = PRODUCT_UPDATE_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.UPDATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PutMapping(path = PRODUCT_UPDATE_BY_ID_SUB_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAPI> updateProductById(@ModelAttribute @Validated UpdateProductRequest body,
                                                         @PathVariable("productId") String id) {
        try {
            ProductCollection data = modelMapper.map(body, ProductCollection.class);
            data.setId(id);
            ProductCollection updatedData = productService.updateProductById(data, body.getImages());
            UpdateProductResponse resData = modelMapper.map(updatedData, UpdateProductResponse.class);
            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.UPDATED)
                    .timestamp(new Date())
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
