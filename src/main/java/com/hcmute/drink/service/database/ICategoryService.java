package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.CreateCategoryRequest;
import com.hcmute.drink.dto.request.UpdateCategoryRequest;
import com.hcmute.drink.dto.response.CreateCategoryResponse;
import com.hcmute.drink.dto.response.GetAllCategoriesWithoutDisabledResponse;
import com.hcmute.drink.dto.response.GetAllCategoryResponse;
import com.hcmute.drink.dto.response.UpdateCategoryResponse;

import java.util.List;

public interface ICategoryService {
    CreateCategoryResponse createCategory(CreateCategoryRequest body) ;
    GetAllCategoryResponse getCategoryById(String id);
    List<GetAllCategoryResponse> getAllCategories();
    List<GetAllCategoriesWithoutDisabledResponse> getAllCategoriesWithoutDeleted();
    UpdateCategoryResponse updateCategory(UpdateCategoryRequest data, String id);
    void deleteCategoryById(String id);
}
