package com.hcmute.drink.service.elasticsearch;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.model.elasticsearch.ProductIndex;
import com.hcmute.drink.repository.elasticsearch.ProductSearchRepository;
import com.hcmute.drink.service.common.CloudinaryService;
import com.hcmute.drink.service.database.implement.ProductService;
import com.hcmute.drink.service.common.ModelMapperService;
import com.hcmute.drink.utils.RegexUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;
    private final ModelMapperService modelMapperService;
    private final CloudinaryService cloudinaryService;

    public ProductIndex createProduct(ProductCollection data) {
        double price = ProductService.getMinPrice(data.getSizeList());

        ProductIndex dataSearch = ProductIndex.builder()
                .id(data.getId())
                .name(data.getName())
                .thumbnailUrl(cloudinaryService.getThumbnailUrl(data.getImageId()))
                .code(data.getCode())
                .description(data.getDescription())
                .status(data.getStatus())
                .categoryId(data.getCategoryId().toString())
                .isDeleted(data.isDeleted())
                .price(price)
                .build();

        return productSearchRepository.save(dataSearch);
    }

    public void upsertProduct(ProductCollection data) {
        ProductIndex product = productSearchRepository.findById(data.getId()).orElse(null);
        if (product != null) {
            product.setName(data.getName());
            product.setThumbnailUrl(cloudinaryService.getThumbnailUrl(data.getImageId()));
            product.setStatus(data.getStatus());
            product.setPrice(data.getPrice());
            product.setDescription(data.getDescription());
            product.setDeleted(data.isDeleted());
            product.setCategoryId(data.getCategoryId().toString());
            productSearchRepository.save(product);
        } else {
            createProduct(data);
        }
    }

    public void deleteProduct(String id) {
        ProductIndex productIndex = productSearchRepository.findById(id).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
        productIndex.setDeleted(true);
        productSearchRepository.save(productIndex);
    }

    public List<ProductIndex> searchVisibleProduct(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        String textRegex = RegexUtils.generateFilterRegexString(key);
        return productSearchRepository.searchVisibleProduct(key, textRegex, pageable).getContent();
    }

    public Page<ProductIndex> searchProduct(String key, String categoryIdRegex, String productStatusRegex, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productSearchRepository.searchProduct(key, categoryIdRegex, productStatusRegex, pageable);
    }
}
