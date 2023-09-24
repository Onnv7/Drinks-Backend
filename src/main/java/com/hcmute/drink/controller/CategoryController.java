package com.hcmute.drink.controller;

import com.hcmute.drink.dto.CreateCategoryRequest;
import com.hcmute.drink.service.impl.CategoryServiceImpl;
import com.hcmute.drink.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @PostMapping
    public void createCategory(@ModelAttribute CreateCategoryRequest body) {
        try {
            categoryService.createCategory(body);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
