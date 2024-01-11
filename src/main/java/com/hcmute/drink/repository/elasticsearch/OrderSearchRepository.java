package com.hcmute.drink.repository.elasticsearch;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.model.elasticsearch.OrderIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OrderSearchRepository extends ElasticsearchRepository<OrderIndex, String> {

}
