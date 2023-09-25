package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.CreateCategoryRequest;
import com.hcmute.drink.dto.UpdateCategoryRequest;
import com.hcmute.drink.repository.CategoryRepository;
import com.hcmute.drink.service.CategoryService;
import com.hcmute.drink.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CloudinaryUtils cloudinaryUtils;

    public CategoryCollection createCategory(CreateCategoryRequest data) throws Exception {
        String cgrName = data.getName();
        CategoryCollection existedCategory = categoryRepository.findByName(cgrName);
        if(existedCategory != null) {
            throw new Exception(ErrorConstant.CATEGORY_EXISTED);
        }
        HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.CATEGORY_PATH, cgrName, data.getImage());
        ImageModel imageModel = new ImageModel(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
        CategoryCollection category = CategoryCollection.builder()
                .image(imageModel)
                .name(data.getName())
                .build();
        CategoryCollection newCategory = categoryRepository.save(category);
        if(newCategory == null) {
            throw new Exception(ErrorConstant.CREATED_FAILED);
        }
        return category;
    }

    public CategoryCollection getCategoryById(String id) throws Exception {
        CategoryCollection category = categoryRepository.findById(id).orElse(null);
        if(category == null) {
            throw new Exception(ErrorConstant.NOT_FOUND);
        }
        return category;
    }

    public List<CategoryCollection> getAllCategories() {
        return categoryRepository.findAll();
    }

    public CategoryCollection updateCategory(UpdateCategoryRequest data, String id) throws Exception {
        CategoryCollection category = categoryRepository.findById(id).orElse(null);
        if(category == null) {
            throw new Exception(ErrorConstant.CATEGORY_NOT_FOUND);
        }
        cloudinaryUtils.deleteImage(category.getImage().getId());
        HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.CATEGORY_PATH, data.getName(), data.getImage());
        category.setName(data.getName());
        ImageModel imageModel = new ImageModel(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
        category.setImage(imageModel);
        CategoryCollection newCategory = categoryRepository.save(category);

        if(newCategory != null) {
            return category;
        }
        throw new Exception(ErrorConstant.UPDATE_FAILED);
    }

    public boolean deleteCategoryById(String id) throws Exception {
        CategoryCollection category = categoryRepository.findById(id).orElse(null);
        if(category == null) {
            throw new Exception(ErrorConstant.NOT_FOUND);
        }
        cloudinaryUtils.deleteImage(category.getImage().getId());
        categoryRepository.deleteById(id);
        return true;
    }
}
