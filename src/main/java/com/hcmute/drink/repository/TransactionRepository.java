package com.hcmute.drink.repository;

import com.hcmute.drink.collection.TransactionCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionCollection, String> {
}
