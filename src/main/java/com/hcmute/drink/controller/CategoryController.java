package com.hcmute.drink.controller;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.CreateCategoryRequest;
import com.hcmute.drink.dto.UpdateCategoryRequest;
import com.hcmute.drink.model.ApiResponse;
import com.hcmute.drink.service.impl.CategoryServiceImpl;
import com.hcmute.drink.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse> createCategory(@ModelAttribute CreateCategoryRequest body) {
        try {
            CategoryCollection category = categoryService.createCategory(body);
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .data(category)
                    .message(SuccessConstant.CREATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.CREATED);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable("categoryId") String categoryId) {
        try {
            CategoryCollection category = categoryService.getCategoryById(categoryId);
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.GET)
                    .data(category)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<CategoryCollection> list = categoryService.getAllCategories();

            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.GET)
                    .data(list)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@ModelAttribute @Validated UpdateCategoryRequest body,
                                                      @PathVariable("categoryId") String id) {
        try {
            CategoryCollection data = categoryService.updateCategory(body, id);
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.UPDATED)
                    .data(data)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable("categoryId") String id) {
        try {
            categoryService.deleteCategoryById(id);
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.DELETED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
