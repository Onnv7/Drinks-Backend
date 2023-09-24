package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.constant.CloudinaryConstant;
import com.hcmute.drink.dto.CreateCategoryRequest;
import com.hcmute.drink.repository.CategoryRepository;
import com.hcmute.drink.service.CategoryService;
import com.hcmute.drink.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CloudinaryUtils cloudinaryUtils;

    public void createCategory(CreateCategoryRequest data) throws IOException {
        // FIXME: thêm check sự ồn tại trước khi upload ảnh category
        String imageUrl = cloudinaryUtils.uploadFileToFolder(CloudinaryConstant.CATEGORY_PATH, data.getName(), data.getImage());
        CategoryCollection category = CategoryCollection.builder()
                .imageUrl(imageUrl)
                .name(data.getName())
                .build();
        categoryRepository.save(category);

    }
}
