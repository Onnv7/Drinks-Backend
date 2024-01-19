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
               "must_not": [
                 { "match": { "status": "HIDDEN" } }
               ],
               "should": [
                 { "match": { "name": "?0 " } },
                 { "match": { "description": "?0" } },
                 { "regexp": { "code": "?1" } }
               ],
               "minimum_should_match": 1
             }
               
        }
    """)
    Page<ProductIndex> searchVisibleProduct(String key, String code, Pageable page);

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
        // TODO: xem lại chỗ code có nên regex như category id không
    Page<ProductIndex> searchProduct(String key, String categoryIdRegex, String productStatusRegex, Pageable page);
}
