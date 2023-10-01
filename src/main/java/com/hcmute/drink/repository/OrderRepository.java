package com.hcmute.drink.repository;

import com.hcmute.drink.collection.OrderCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderCollection, String> {
    OrderCollection findByTransactionId(String transactionId);
}
