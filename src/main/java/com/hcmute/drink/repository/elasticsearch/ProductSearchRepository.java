package com.hcmute.drink.repository.elasticsearch;

import com.hcmute.drink.dto.response.GetAllProductsResponse;
import com.hcmute.drink.model.elasticsearch.ProductIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

//@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductIndex, String> {
    @Query("""
            {
                "bool": {
                  "should": [
                    { "match": { "name": "?0" }},
                    { "match": { "description": "?0" }},
                    { "match": { "code": "?0" }}
                  ],
                  "must_not": [
                      { "match": { "status": "HIDDEN" }}
                    ]
                }
            }
            """)
    @SourceFilters(excludes = {"description"})
    Page<ProductIndex> searchVisibleProduct(String key, Pageable page);
    @Query("""
            {
                "bool": {
                  "should": [
                    { "match": { "name": "?0" }},
                    { "match": { "description": "?0" }},
                    { "match": { "code": "?0" }}
                  ]
                }
            }
            """)
    @SourceFilters(excludes = {"description"})
    Page<ProductIndex> searchProduct(String key, Pageable page);
}
