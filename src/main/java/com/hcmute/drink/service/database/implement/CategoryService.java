package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateCategoryRequest;
import com.hcmute.drink.dto.request.UpdateCategoryRequest;
import com.hcmute.drink.dto.response.CreateCategoryResponse;
import com.hcmute.drink.dto.response.GetAllCategoriesWithoutDisabledResponse;
import com.hcmute.drink.dto.response.GetAllCategoryResponse;
import com.hcmute.drink.dto.response.UpdateCategoryResponse;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.CategoryRepository;
import com.hcmute.drink.service.database.ICategoryService;
import com.hcmute.drink.utils.CloudinaryUtils;
import com.hcmute.drink.utils.ImageUtils;
import com.hcmute.drink.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final SequenceService sequenceService;
    private final CategoryRepository categoryRepository;
    private final CloudinaryUtils cloudinaryUtils;
    private final ModelMapperUtils modelMapperUtils;

    Date currentDate = new Date();
    long currentTimeMillis = currentDate.getTime();

    public CategoryCollection getById(String id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }

    public CategoryCollection getByName(String name)   {
        return categoryRepository.findByName(name).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + name));
    }
    // SERVICES =================================================================================

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest body) {
        String cgrName = body.getName();
        CategoryCollection existedCategory = categoryRepository.findByName(cgrName).orElse(null);
        if (existedCategory != null) {
            throw new CustomException(ErrorConstant.CATEGORY_EXISTED);
        }
        byte[] originalImage = new byte[0];
        try {
            originalImage = body.getImage().getBytes();
            byte[] newImage = ImageUtils.resizeImage(originalImage, 200, 200);
            HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.CATEGORY_PATH, cgrName + "_" + currentTimeMillis, newImage);
            ImageEmbedded imageEmbedded = new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
            CategoryCollection category = CategoryCollection.builder()
                    .code(sequenceService.generateCode(CategoryCollection.SEQUENCE_NAME, CategoryCollection.PREFIX_CODE, CategoryCollection.LENGTH_NUMBER))
                    .image(imageEmbedded)
                    .name(body.getName())
                    .enabled(true)
                    .build();

            return modelMapperUtils.mapClass(categoryRepository.save(category), CreateCategoryResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GetAllCategoryResponse getCategoryById(String id) {
        CategoryCollection category = getById(id);
        return modelMapperUtils.mapClass(category, GetAllCategoryResponse.class);
    }

    @Override
    public List<GetAllCategoryResponse> getAllCategories() {
        List<CategoryCollection> categoryList = categoryRepository.findAll();
        return modelMapperUtils.mapList(categoryList, GetAllCategoryResponse.class);
    }

    @Override
    public List<GetAllCategoriesWithoutDisabledResponse> getAllCategoriesWithoutDeleted() {
        return categoryRepository.getAllCategoriesWithoutDisabled();
    }

    @Override
    public UpdateCategoryResponse updateCategory(UpdateCategoryRequest data, String id) {
        CategoryCollection category = getById(id);
        if (data.getImage() != null) {
            try {
                cloudinaryUtils.deleteImage(category.getImage().getId());
                byte[] originalImage = data.getImage().getBytes();
                byte[] newImage = ImageUtils.resizeImage(originalImage, 200, 200);

                HashMap<String, String> fileUploaded = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.CATEGORY_PATH, data.getName() + "_" + currentTimeMillis, newImage);
                ImageEmbedded imageEmbedded = new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
                category.setImage(imageEmbedded);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        category.setName(data.getName());
        category.setEnabled(data.isEnabled());
        CategoryCollection newCategory = categoryRepository.save(category);

        if (newCategory != null) {

            return modelMapperUtils.mapClass(data, UpdateCategoryResponse.class);
        }
        throw new CustomException(ErrorConstant.UPDATE_FAILED);
    }

    @Override
    public void deleteCategoryById(String id) {
        CategoryCollection category = getById(id);
        try {
            cloudinaryUtils.deleteImage(category.getImage().getId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        categoryRepository.deleteById(id);
    }
}
