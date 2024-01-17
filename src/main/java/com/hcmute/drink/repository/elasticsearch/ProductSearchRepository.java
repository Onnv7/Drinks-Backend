package com.hcmute.drink.repository.elasticsearch;

import com.hcmute.drink.model.elasticsearch.ProductIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
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
    Page<ProductIndex> searchVisibleProduct(String key, Pageable page);

    @Query("""
            {
                "bool": {
                   "must": [
                        { "multi_match": { "query": "?0", "fields": ["name", "description", "code"] }},
                        { "regexp": { "categoryId": "?1" } },
                        { "regexp": { "status": "?2" } }
                   ]
                }
            }
            """)
    Page<ProductIndex> searchProduct(String key, String categoryIdRegex, String productStatusRegex, Pageable page);
}
