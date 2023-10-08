package com.hcmute.drink.repository;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.dto.GetProductsByCategoryIdResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductCollection, String> {
    @Aggregation(pipeline = {
            "{$match: {categoryId: ?0}}",
            "{$project: {_id: 1, name: 1, description: 1, price: {$min: '$sizeList.price'}, imageUrl: {$first: '$imageList.url'}}}"
    })
    List<GetProductsByCategoryIdResponse> getProductsByCategoryId(ObjectId categoryId);
}
