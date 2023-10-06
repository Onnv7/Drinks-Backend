package com.hcmute.drink.repository;

import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.enums.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionCollection, String> {

}
