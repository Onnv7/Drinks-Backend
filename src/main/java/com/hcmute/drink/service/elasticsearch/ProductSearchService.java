package com.hcmute.drink.service.elasticsearch;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.response.GetAllVisibleProductResponse;
import com.hcmute.drink.dto.response.GetAllProductsResponse;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.model.elasticsearch.ProductIndex;
import com.hcmute.drink.repository.elasticsearch.ProductSearchRepository;
import com.hcmute.drink.service.database.implement.ProductService;
import com.hcmute.drink.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductSearchRepository productSearchRepository;
    private final ModelMapperUtils modelMapperUtils;

    public ProductIndex createProduct(ProductCollection data) {
        double price = ProductService.getMinPrice(data.getSizeList());

        ProductIndex dataSearch = ProductIndex.builder()
                .id(data.getId())
                .name(data.getName())
                .thumbnail(data.getThumbnail().getUrl())
                .code(data.getCode())
                .description(data.getDescription())
                .status(data.getStatus())
                .price(price)
                .build();

        return productSearchRepository.save(dataSearch);
    }

    public void upsertProduct(ProductCollection data) {
        ProductIndex product = ProductIndex.builder()
                .id(data.getId())
                .name(data.getName())
                .thumbnail(data.getThumbnail().getUrl())
                .code(data.getCode())
                .status(data.getStatus())
                .build();
        productSearchRepository.save(product);
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
    public List<ProductIndex> searchProduct(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return productSearchRepository.searchProduct(key, pageable).getContent();
    }
}
