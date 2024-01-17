package com.hcmute.drink.controller;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.request.CreateCategoryRequest;
import com.hcmute.drink.dto.request.UpdateCategoryRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.database.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = CATEGORY_CONTROLLER_TITLE)
@RestController
@RequestMapping(CATEGORY_BASE_PATH)
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @Operation(summary = CATEGORY_CREATE_SUM)
    @PostMapping(path = POST_CATEGORY_CREATE_SUB_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAPI> createCategory(@ModelAttribute @Valid CreateCategoryRequest body) {
        categoryService.createCategory(body);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.CREATED)
                .build();
        return new ResponseEntity<>(res, StatusCode.CREATED);
    }

    @Operation(summary = CATEGORY_GET_BY_ID_SUM)
    @GetMapping(GET_CATEGORY_BY_SUB_ID_PATH)
    public ResponseEntity<ResponseAPI> getCategoryById(@PathVariable(CATEGORY_ID) String categoryId) {
        GetCategoryByIdResponse resData = categoryService.getCategoryById(categoryId);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.GET)
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = CATEGORY_GET_ALL_SUM)
    @GetMapping(path = GET_CATEGORY_ALL_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAllCategories() {
        List<GetAllCategoryResponse> resData = categoryService.getAllCategories();

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.GET)
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = CATEGORY_GET_ALL_WITHOUT_DELETED_SUM)
    @GetMapping(path = GET_CATEGORY_ALL_WITHOUT_DELETED_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAllCategoriesWithoutDeleted() {
        List<GetVisibleCategoryListResponse> resData = categoryService.getVisibleCategoryList();
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.GET)
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = CATEGORY_UPDATE_BY_ID_SUM)
    @PutMapping(path = PUT_CATEGORY_UPDATE_BY_ID_SUB_PATH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseAPI> updateCategory(
            @ModelAttribute @Valid UpdateCategoryRequest body,
            @PathVariable(CATEGORY_ID) String id) {
        categoryService.updateCategory(body, id);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.UPDATED)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = CATEGORY_DELETE_BY_ID_SUM)
    @DeleteMapping(path = DELETE_CATEGORY_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> deleteCategoryById(@PathVariable(CATEGORY_ID) String id) {
            categoryService.deleteCategoryById(id);
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.DELETED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);

    }

}
