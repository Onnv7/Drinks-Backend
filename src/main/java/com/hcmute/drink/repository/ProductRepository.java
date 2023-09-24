package com.hcmute.drink.repository;

import com.hcmute.drink.collection.ProductCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<ProductCollection, String> {
}
