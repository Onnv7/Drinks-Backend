package com.hcmute.drink.repository;

import com.hcmute.drink.collection.CategoryCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends MongoRepository<CategoryCollection, String> {
}
