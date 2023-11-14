package com.hcmute.drink.controller;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.OrderService;
import com.hcmute.drink.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = PRODUCT_CONTROLLER_TITLE)
@RestController
@RequestMapping(PRODUCT_BASE_PATH)
@RequiredArgsConstructor
public class ProductController {
    private final ModelMapper modelMapper;
    private final ProductService productService;


    @Operation(summary = PRODUCT_CREATE_SUM, description = PRODUCT_CREATE_DES)
    @ApiResponse(responseCode = StatusCode.CODE_CREATED, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = PRODUCT_CREATE_SUB_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAPI> createProduct(@ModelAttribute @Validated CreateProductRequest body) {
        try {
            ProductCollection product = modelMapper.map(body, ProductCollection.class);

            product = productService.createProduct(product, body.getImage());
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
    @GetMapping(path = PRODUCT_GET_DETAILS_BY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getProductDetailsById(@PathVariable("productId") String id) {
        try {
            GetProductByIdResponse resData = productService.getProductDetailsById(id);

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

    @Operation(summary = PRODUCT_GET_ENABLED_BY_ID_SUM, description = PRODUCT_GET_ENABLED_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = PRODUCT_GET_ENABLED_BY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getProductEnabledById(@PathVariable("productId") String id) {
        try {
            GetProductEnabledByIdResponse resData = productService.getProductEnabledById(id);
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
            List<GetProductsByCategoryIdResponse> products = productService.getProductsByCategoryId(categoryId);

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

    @Operation(summary = PRODUCT_GET_ALL_OR_SEARCH_ENABLED_SUM, description = PRODUCT_GET_ALL_OR_SEARCH_ENABLED_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = PRODUCT_GET_ALL_ENABLED_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getAllProductsEnabled(
            @Parameter(name = "key", description = "Key is name or description",required = false, example = "name or description")
            @RequestParam(name = "key", required = false) String key,
            @Parameter(name = "page", required = true, example = "1")
            @RequestParam("page") int page,
            @Parameter(name = "size", required = true, example = "10")
            @RequestParam("size") int size
    ) {
        try {
            if(page <= 0 || size <= 0) {
                throw new RuntimeException("Invalid's page or size");
            }
            List<GetAllProductsEnabledResponse> resData = new ArrayList<>();
            if(key == null) {
                resData = productService.getAllProductsEnabled(page, size);
            } else {
                resData = productService.searchProductByNameOrDescription(key, page, size);
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

    @Operation(summary = PRODUCT_GET_ALL_SUM, description = PRODUCT_GET_ALL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = PRODUCT_GET_ALL_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getAllProducts() {
        try {
            List<GetAllProductsResponse> resData = productService.getAllProducts();

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
            ProductCollection updatedData = productService.updateProductById(body, id);
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
    @Operation(summary = PRODUCT_GET_TOP_ORDER_QUANTITY_SUM, description = PRODUCT_GET_TOP_ORDER_QUANTITY_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = PRODUCT_GET_TOP_QUANTITY_ORDER_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getTopProductQuantityOrder(
            @Parameter(name = "itemQuantity", required = true, example = "10")
            @PathVariable("itemQuantity") int itemQuantity
    ) {
        try {
            if(itemQuantity <= 0) {
                throw new RuntimeException("Invalid's itemQuantity");
            }

            List<GetAllProductsEnabledResponse>  resData = productService.getTopProductQuantityOrder(itemQuantity);

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
}
