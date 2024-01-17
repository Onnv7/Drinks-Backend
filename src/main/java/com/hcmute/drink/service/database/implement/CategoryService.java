package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.CreateCategoryRequest;
import com.hcmute.drink.dto.request.UpdateCategoryRequest;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.CategoryStatus;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.CategoryRepository;
import com.hcmute.drink.service.database.ICategoryService;
import com.hcmute.drink.service.common.CloudinaryService;
import com.hcmute.drink.utils.ImageUtils;
import com.hcmute.drink.service.common.ModelMapperService;
import com.hcmute.drink.utils.StringUtils;
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
    private final CloudinaryService cloudinaryService;
    private final ModelMapperService modelMapperService;

    Date currentDate = new Date();
    long currentTimeMillis = currentDate.getTime();

    public CategoryCollection getById(String id) {
        return categoryRepository.getById(id).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }
    public CategoryCollection saveCategory(CategoryCollection data) {
        return categoryRepository.save(data);
    }


    // SERVICES =================================================================================

    @Override
    public CreateCategoryResponse createCategory(CreateCategoryRequest body) {
        String cgrName = body.getName();
        CategoryCollection existedCategory = categoryRepository.getByName(cgrName).orElse(null);
        if (existedCategory != null) {
            throw new CustomException(ErrorConstant.CATEGORY_EXISTED);
        }
        byte[] originalImage = new byte[0];
        try {
            originalImage = body.getImage().getBytes();
            byte[] newImage = ImageUtils.resizeImage(originalImage, 200, 200);
            HashMap<String, String> fileUploaded = cloudinaryService.uploadFileToFolder(CloudinaryConstant.CATEGORY_PATH,
                    StringUtils.generateFileName(body.getName(), "category"), newImage);
            ImageEmbedded imageEmbedded = new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
            CategoryCollection category = CategoryCollection.builder()
                    .code(sequenceService.generateCode(CategoryCollection.SEQUENCE_NAME, CategoryCollection.PREFIX_CODE, CategoryCollection.LENGTH_NUMBER))
                    .image(imageEmbedded)
                    .name(body.getName())
                    .status(CategoryStatus.HIDDEN)
                    .canDelete(true)
                    .isDeleted(false)
                    .build();

            return modelMapperService.mapClass(categoryRepository.save(category), CreateCategoryResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GetCategoryByIdResponse getCategoryById(String id) {
        CategoryCollection category = getById(id);
        return modelMapperService.mapClass(category, GetCategoryByIdResponse.class);
    }

    @Override
    public List<GetAllCategoryResponse> getAllCategories() {
        List<CategoryCollection> categoryList = categoryRepository.getAll();
        return modelMapperService.mapList(categoryList, GetAllCategoryResponse.class);
    }

    @Override
    public List<GetVisibleCategoryListResponse> getVisibleCategoryList() {
        return categoryRepository.getVisibleCategoryList();
    }

    @Override
    public void updateCategory(UpdateCategoryRequest body, String id) {
        CategoryCollection category = getById(id);
        if (body.getImage() != null) {
            try {
                cloudinaryService.deleteImage(category.getImage().getId());
                byte[] originalImage = body.getImage().getBytes();
                byte[] newImage = ImageUtils.resizeImage(originalImage, 200, 200);

                HashMap<String, String> fileUploaded = cloudinaryService.uploadFileToFolder(CloudinaryConstant.CATEGORY_PATH, StringUtils.generateFileName(body.getName(), "category"), newImage);
                ImageEmbedded imageEmbedded = new ImageEmbedded(fileUploaded.get(CloudinaryConstant.PUBLIC_ID), fileUploaded.get(CloudinaryConstant.URL_PROPERTY));
                category.setImage(imageEmbedded);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        category.setName(body.getName());
        category.setStatus(body.getStatus());
        CategoryCollection newCategory = categoryRepository.save(category);
//        return modelMapperUtils.mapClass(data, UpdateCategoryResponse.class);
    }

    @Override
    public void deleteCategoryById(String id) {
        CategoryCollection category = getById(id);
        if(category.isCanDelete()) {
            category.setDeleted(true);
            categoryRepository.save(category);
        } else {
            throw new CustomException(ErrorConstant.CANT_DELETE);
        }
    }
}
