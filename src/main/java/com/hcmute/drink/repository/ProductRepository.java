package com.hcmute.drink.repository;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.dto.GetProductsByCategoryIdResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductCollection, String> {
    List<GetProductsByCategoryIdResponse> findByCategoryId(ObjectId categoryId);
}
