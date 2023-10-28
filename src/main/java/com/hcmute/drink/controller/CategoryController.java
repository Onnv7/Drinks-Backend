package com.hcmute.drink.controller;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.impl.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = CATEGORY_CONTROLLER_TITLE)
@RestController
@RequestMapping(CATEGORY_BASE_PATH)
@RequiredArgsConstructor
public class CategoryController {
    private final ModelMapper modelMapper;
    private final CategoryServiceImpl categoryService;

    @Operation(summary = CATEGORY_CREATE_SUM, description = CATEGORY_CREATE_DES)
    @ApiResponse(responseCode = StatusCode.CODE_CREATED, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = CATEGORY_CREATE_SUB_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAPI> createCategory(@ModelAttribute @Validated CreateCategoryRequest body) {
        try {
            CategoryCollection category = categoryService.createCategory(body);
            CreateCategoryResponse resData = modelMapper.map(category, CreateCategoryResponse.class);

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(resData)
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = CATEGORY_GET_BY_ID_SUM, description = CATEGORY_GET_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_CREATED, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(CATEGORY_GET_BY_SUB_ID_PATH)
    public ResponseEntity<ResponseAPI> getCategoryById(@PathVariable("categoryId") String categoryId) {
        try {
            CategoryCollection category = categoryService.getCategoryById(categoryId);
            GetAllCategoryResponse resData = modelMapper.map(category, GetAllCategoryResponse.class);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.GET)
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = CATEGORY_GET_ALL_SUM, description = CATEGORY_GET_ALL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = CATEGORY_GET_ALL_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAllCategories() {
        try {
            List<CategoryCollection> list = categoryService.getAllCategories();
            List<GetAllCategoryResponse> resData = new ArrayList<>();
            for (CategoryCollection category : list) {
                resData.add(modelMapper.map(category, GetAllCategoryResponse.class));
            }
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.GET)
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = CATEGORY_GET_ALL_WITHOUT_DELETED_SUM, description = CATEGORY_GET_ALL_WITHOUT_DELETED_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = CATEGORY_GET_ALL_WITHOUT_DELETED_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAllCategoriesWithoutDeleted() {
        try {
            List<GetAllCategoriesWithoutDisabledResponse> resData = categoryService.getAllCategoriesWithoutDeleted();
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.GET)
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = CATEGORY_UPDATE_BY_ID_SUM, description = CATEGORY_UPDATE_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.UPDATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PutMapping(path = CATEGORY_UPDATE_BY_ID_SUB_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAPI> updateCategory(@ModelAttribute @Validated UpdateCategoryRequest body,
                                                      @PathVariable("categoryId") String id) {
        try {
            CategoryCollection data = categoryService.updateCategory(body, id);
            UpdateCategoryResponse resData = modelMapper.map(data, UpdateCategoryResponse.class);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.UPDATED)
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = CATEGORY_DELETE_BY_ID_SUM, description = CATEGORY_DELETE_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.DELETED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @DeleteMapping(path = CATEGORY_DELETE_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> deleteCategoryById(@PathVariable("categoryId") String id) {
        try {
            categoryService.deleteCategoryById(id);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.DELETED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = CATEGORY_SOFT_DELETE_BY_ID_SUM, description = CATEGORY_SOFT_DELETE_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.DELETED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @DeleteMapping(path = CATEGORY_SOFT_DELETE_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> softDeleteCategoryById(@PathVariable("categoryId") String id) {
        try {
            categoryService.softDeleteCategoryById(id);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.DELETED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
