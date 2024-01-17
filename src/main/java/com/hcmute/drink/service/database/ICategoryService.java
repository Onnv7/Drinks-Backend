package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.CreateCategoryRequest;
import com.hcmute.drink.dto.request.UpdateCategoryRequest;
import com.hcmute.drink.dto.response.*;

import java.util.List;

public interface ICategoryService {
    CreateCategoryResponse createCategory(CreateCategoryRequest body) ;
    GetCategoryByIdResponse getCategoryById(String id);
    List<GetAllCategoryResponse> getAllCategories();
    List<GetVisibleCategoryListResponse> getVisibleCategoryList();
    void updateCategory(UpdateCategoryRequest body, String id);
    void deleteCategoryById(String id);
}
