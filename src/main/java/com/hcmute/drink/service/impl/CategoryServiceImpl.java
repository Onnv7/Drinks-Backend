package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.CreateCategoryRequest;
import com.hcmute.drink.dto.GetAllCategoriesWithoutDisabledResponse;
import com.hcmute.drink.dto.UpdateCategoryRequest;
import com.hcmute.drink.repository.CategoryRepository;
import com.hcmute.drink.service.CategoryService;
import com.hcmute.drink.utils.CloudinaryUtils;
import com.hcmute.drink.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CloudinaryUtils cloudinaryUtils;
    private final ImageUtils imageUtils;

    Date currentDate = new Date();
    long currentTimeMillis = currentDate.getTime();

    public CategoryCollection exceptionIfNotExistedCategoryById(String id) throws Exception {
        CategoryCollection category = categoryRepository.findById(id).orElse(null);
        if(category == null) {
            throw new Exception(ErrorConstant.CATEGORY_NOT_FOUND + " " + id);
        }
        return category;
    }
    public CategoryCollection exceptionIfNotExistedCategoryByName(String name) throws Exception {
        CategoryCollection category = categoryRepository.findByName(name);
        if(category == null) {
            throw new Exception(ErrorConstant.CATEGORY_NOT_FOUND + " " + name);
        }
        return category;
    }
    // SERVICES =================================================================================

    public CategoryCollection createCategory(CreateCategoryRequest data) throws Exception {
        String cgrName = data.getName();
        CategoryCollection existedCategory = categoryRepository.findByName(cgrName);
        if(existedCategory != null) {
            throw new Exception(ErrorConstant.CATEGORY_EXISTED);
        }
        byte[] originalImage = data.getImage().getBytes();
        byte[] newImage = imageUtils.resizeImage(originalImage, 200, 200);

        HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.CATEGORY_PATH, cgrName + "_" +currentTimeMillis, newImage);
        ImageEmbedded imageEmbedded = new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
        CategoryCollection category = CategoryCollection.builder()
                .image(imageEmbedded)
                .name(data.getName())
                .enabled(true)
                .build();
        return categoryRepository.save(category);
    }

    public CategoryCollection getCategoryById(String id) throws Exception {
        return exceptionIfNotExistedCategoryById(id);
    }

    public List<CategoryCollection> getAllCategories() {
        return categoryRepository.findAll();
    }
    public List<GetAllCategoriesWithoutDisabledResponse> getAllCategoriesWithoutDeleted() {
        return categoryRepository.getAllCategoriesWithoutDisabled();
    }

    public CategoryCollection   updateCategory(UpdateCategoryRequest data, String id) throws Exception {
        CategoryCollection category = exceptionIfNotExistedCategoryById(id);
        if(data.getImage() != null) {
            cloudinaryUtils.deleteImage(category.getImage().getId());

            byte[] originalImage = data.getImage().getBytes();
            byte[] newImage = imageUtils.resizeImage(originalImage, 200, 200);

            HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.CATEGORY_PATH, data.getName() +"_" + currentTimeMillis, newImage);
            ImageEmbedded imageEmbedded = new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
            category.setImage(imageEmbedded);
        }
        category.setName(data.getName());
        category.setEnabled(data.isEnabled());
        CategoryCollection newCategory = categoryRepository.save(category);

        if(newCategory != null) {
            return category;
        }
        throw new Exception(ErrorConstant.UPDATE_FAILED);
    }

    public boolean deleteCategoryById(String id) throws Exception {
        CategoryCollection category = exceptionIfNotExistedCategoryById(id);
        cloudinaryUtils.deleteImage(category.getImage().getId());
        categoryRepository.deleteById(id);
        return true;
    }
}
