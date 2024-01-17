package com.hcmute.drink.service.elasticsearch;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.model.elasticsearch.ProductIndex;
import com.hcmute.drink.repository.elasticsearch.ProductSearchRepository;
import com.hcmute.drink.service.database.implement.ProductService;
import com.hcmute.drink.service.common.ModelMapperService;
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

    public ProductIndex createProduct(ProductCollection data) {
        double price = ProductService.getMinPrice(data.getSizeList());

        ProductIndex dataSearch = ProductIndex.builder()
                .id(data.getId())
                .name(data.getName())
                .thumbnail(data.getThumbnail().getUrl())
                .code(data.getCode())
                .description(data.getDescription())
                .status(data.getStatus())
                .categoryId(data.getCategoryId().toString())
                .price(price)
                .build();

        return productSearchRepository.save(dataSearch);
    }

    public void upsertProduct(ProductCollection data) {
        ProductIndex product = productSearchRepository.findById(data.getId()).orElse(null);
        if(product != null) {
            product.setName(data.getName());
            product.setThumbnail(data.getThumbnail().getUrl());
            product.setStatus(data.getStatus());
            product.setPrice(data.getPrice());
            product.setDescription(data.getDescription());
            product.setCategoryId(data.getCategoryId().toString());
            productSearchRepository.save(product);
        } else {
            createProduct(data);
        }
    }

    public void deleteProduct(String id) {
        if(productSearchRepository.existsById(id)) {
            productSearchRepository.deleteById(id);
        } else {
            throw new CustomException(ErrorConstant.NOT_FOUND + id);
        }
    }

    public List<ProductIndex> searchVisibleProduct(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return productSearchRepository.searchVisibleProduct(key, pageable).getContent();
    }
    public Page<ProductIndex> searchProduct(String key, String categoryIdRegex, String productStatusRegex, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return productSearchRepository.searchProduct(key, categoryIdRegex, productStatusRegex, pageable);
    }
}
