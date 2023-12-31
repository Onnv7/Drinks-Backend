package com.hcmute.drink.repository;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.dto.*;
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
            "{$project: {_id: 1, name: 1, description: 1, price: {$min: '$sizeList.price'}, thumbnail: '$thumbnail.url'}}"
    })
    List<GetProductsByCategoryIdResponse> getProductsByCategoryId(ObjectId categoryId);

    @Aggregation(pipeline = {
            "{$match: {enabled: true}}",
            "{$project: {_id: 1, name: 1, description: 1, price: {$min: '$sizeList.price'}, thumbnail: '$thumbnail.url'}}",
            "{$skip: ?0}",
            "{$limit: ?1}",
    })
    List<GetAllProductsEnabledResponse> getAllProductsEnabled(int skip, int limit);

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
    @Query(value = "{$and : [{_id: ?0}]}", fields = "{_id: 1, name: 1, sizeList: 1, toppingList: 1, description: 1, image: 1, categoryId: 1, enabled: 1}}")
    GetProductByIdResponse getProductDetailsById(String id);

    @Aggregation(pipeline = {
            "{$match: {$or: [{name: {$regex: ?0, $options: 'i'}}, {description: {$regex: ?0, $options: 'i'}}]}}",
            "{$project: {_id: 1, name: 1, description: 1, price: {$min: '$sizeList.price'}, thumbnail: '$thumbnail.url'}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
    })
    List<GetAllProductsEnabledResponse> searchProductByNameOrDescription(String key, int skip, int limit);
    @Aggregation(pipeline = {
            "{$match: {_id: { $nin: ?1 }}}",
            "{$project: {_id: 1, name: 1, price: {$min: '$sizeList.price'}, description: 1, thumbnail: '$thumbnail.url'}}",
            "{$limit: ?0}"
    })

    List<GetAllProductsEnabledResponse> getSomeProduct(int quantity, List<ObjectId> excludeId);
}
