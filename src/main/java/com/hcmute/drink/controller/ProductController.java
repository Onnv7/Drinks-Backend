package com.hcmute.drink.controller;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.request.CreateProductRequest;
import com.hcmute.drink.dto.request.DeleteSomeProductRequest;
import com.hcmute.drink.dto.request.UpdateProductRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.ProductStatus;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.database.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final IProductService productService;


    @Operation(summary = PRODUCT_CREATE_SUM)
    @PostMapping(path = POST_PRODUCT_CREATE_SUB_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAPI> createProduct(@ModelAttribute @Valid CreateProductRequest body) {
        productService.createProduct(body, body.getImage());

        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.CREATED)
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(res, StatusCode.CREATED);
    }

    @Operation(summary = PRODUCT_GET_BY_ID_SUM)
    @GetMapping(path = GET_PRODUCT_DETAILS_BY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getProductDetailsById(@PathVariable(PRODUCT_ID) String id) {
        GetProductByIdResponse resData = productService.getProductDetailsById(id);

        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.GET)
                .timestamp(new Date())
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = PRODUCT_GET_ENABLED_BY_ID_SUM)
    @GetMapping(path = GET_PRODUCT_ENABLED_BY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getProductEnabledById(@PathVariable(PRODUCT_ID) String id) {
        GetProductEnabledByIdResponse resData = productService.getProductEnabledById(id);
        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.GET)
                .timestamp(new Date())
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = PRODUCT_GET_BY_CATEGORY_ID_SUM)
    @GetMapping(path = GET_PRODUCT_BY_CATEGORY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getProductByCategoryId(@PathVariable(CATEGORY_ID) String categoryId) {
        List<GetProductsByCategoryIdResponse> products = productService.getProductsByCategoryId(categoryId);

        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.GET)
                .timestamp(new Date())
                .data(products)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

    @Operation(summary = PRODUCT_GET_ALL_OR_SEARCH_ENABLED_SUM)
    @GetMapping(path = GET_PRODUCT_ALL_VISIBLE_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getAllProductsVisible(
            @Parameter(name = "key", description = "Key is name or description or id", required = false, example = "name or description")
            @RequestParam(name = "key", required = false) String key,
            @Parameter(name = "page", required = true, example = "1")
            @RequestParam("page") @Min(value = 1, message = "Page must be greater than 0") int page,
            @Parameter(name = "size", required = true, example = "10")
            @RequestParam("size") @Min(value = 1, message = "Size must be greater than 0") int size
    ) {
        List<GetAllVisibleProductResponse> resData = productService.getAllProductsVisible(page, size, key);

        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.GET)
                .timestamp(new Date())
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = PRODUCT_GET_ALL_SUM)
    @GetMapping(path = GET_PRODUCT_ALL_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getAllProducts(
            @Parameter(name = "key", description = "Key is name or description", required = false, example = "name or description")
            @RequestParam(name = "key", required = false) String key,
            @Parameter(name = "page", required = true, example = "1")
            @RequestParam("page") @Min(value = 1, message = "Page must be greater than 0") int page,
            @Parameter(name = "size", required = true, example = "10")
            @RequestParam("size") @Min(value = 1, message = "Size must be greater than 0") int size,
            @RequestParam(name = CATEGORY_ID, required = false) String categoryId,
            @RequestParam(name = "productStatus", required = false) ProductStatus productStatus
    ) {
        GetProductListResponse resData = productService.getProductList(key, page, size, categoryId, productStatus);
        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.GET)
                .timestamp(new Date())
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }


    @Operation(summary = PRODUCT_DELETE_BY_ID_SUM)
    @DeleteMapping(path = DELETE_PRODUCT_BY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> deleteProductById(@PathVariable("productId") String id) {
        productService.deleteProductById(id);

        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.DELETED)
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = PRODUCT_SOME_DELETE_BY_ID_SUM)
    @DeleteMapping(path = DELETE_SOME_PRODUCT_BY_ID_SUB_PATH)
    protected ResponseEntity<ResponseAPI> deleteSomeProductById(@RequestBody @Valid DeleteSomeProductRequest body) {
        productService.deleteSomeProductById(body.getProductIdList());

        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.DELETED)
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = PRODUCT_UPDATE_BY_ID_SUM)
    @PutMapping(path = PUT_PRODUCT_UPDATE_BY_ID_SUB_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAPI> updateProductById(
            @ModelAttribute @Valid UpdateProductRequest body,
            @PathVariable("productId") String id
    ) {
        productService.updateProductById(body, id);
        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.UPDATED)
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = PRODUCT_GET_TOP_ORDER_QUANTITY_SUM)
    @GetMapping(path = GET_PRODUCT_TOP_QUANTITY_ORDER_SUB_PATH)
    protected ResponseEntity<ResponseAPI> getTopProductQuantityOrder(
            @Parameter(name = "itemQuantity", required = true, example = "10")
            @PathVariable("itemQuantity") @Min(value = 1, message = "Page must be greater than 0") int itemQuantity
    ) {
        List<GetAllVisibleProductResponse> resData = productService.getTopProductQuantityOrder(itemQuantity);

        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.GET)
                .timestamp(new Date())
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }
}
