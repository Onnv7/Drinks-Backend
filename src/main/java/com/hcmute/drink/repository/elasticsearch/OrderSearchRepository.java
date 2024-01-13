package com.hcmute.drink.repository.elasticsearch;

import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.model.elasticsearch.OrderIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.client.ClientLogger;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSearchRepository extends ElasticsearchRepository<OrderIndex, String> {
    @Query("""
        {
            "bool": {
                "should": [
                    { "match": { "code": "?0" }},
                    { "match": { "customerName": "?0" }},
                    { "match": { "customerCode": "?0" }},
                    { "match": { "phoneNumber": "?0" }},
                    { "match": { "recipientName": "?0" }}
                ],
                "must": [
                    { "match": { "statusLastEvent": "?1" }}
                ]
            }
        }
        """)
    Page<OrderIndex> searchOrder(String key, OrderStatus status, Pageable page);

}
