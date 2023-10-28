package com.hcmute.drink.repository;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.dto.GetAllProductsEnabledResponse;
import com.hcmute.drink.dto.GetAllProductsResponse;
import com.hcmute.drink.dto.GetProductEnabledByIdResponse;
import com.hcmute.drink.dto.GetProductsByCategoryIdResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductCollection, String> {
    @Aggregation(pipeline = {
            "{$match: {categoryId: ?0}}",
            "{$project: {_id: 1, name: 1, description: 1, price: {$min: '$sizeList.price'}, image: '$image.url'}"
    })
    List<GetProductsByCategoryIdResponse> getProductsByCategoryId(ObjectId categoryId);

    @Aggregation(pipeline = {
            "{$match: {enabled: true}}",
            "{$project: {_id: 1, name: 1, description: 1, price: {$min: '$sizeList.price'}, image: '$image.url'}"
    })
    List<GetAllProductsEnabledResponse> getAllProductsEnabled();

    @Aggregation(pipeline = {
            "{$project: {_id: 1, name: 1, description: 1, price: {$min: '$sizeList.price'}, image: '$image.url', thumbnail: '$thumbnail.url', enabled: 1}}"
    })
    List<GetAllProductsResponse> getAllProducts();

//    @Aggregation(pipeline = {
//            "{$match: {_id: ?0, enabled: true}}",
//            "{$project: {_id: 1, name: 1, sizeList: 1, toppingList: 1, description: 1, image: '$image.url'}}"
//    })
    @Query(value = "{$and : [{_id: ?0, enabled: true}]}", fields = "{_id: 1, name: 1, sizeList: 1, toppingList: 1, description: 1, image: '$image.url'}}")
    GetProductEnabledByIdResponse getProductEnabledById(String id);


}
