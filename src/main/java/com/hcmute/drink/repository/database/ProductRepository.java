package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.dto.response.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductCollection, String> {
    @Aggregation(pipeline = {
            "{$match: {$and: [{categoryId: ?0, status: {$ne: 'HIDDEN'}]",
            "{$project: {_id: 1, name: 1, description: 1, status: 1, price: {$min: '$sizeList.price'}, thumbnail: '$thumbnail.url'}}"
    })
    List<GetProductsByCategoryIdResponse> getProductsByCategoryId(ObjectId categoryId);

    @Aggregation(pipeline = {
            "{$match: { status: {$ne: 'HIDDEN'} }}",
            "{$project: {_id: 1, name: 1, description: 1, status: 1, price: {$min: '$sizeList.price'}, thumbnail: '$thumbnail.url'}}",
            "{$skip: ?0}",
            "{$limit: ?1}",
    })
    List<GetAllVisibleProductResponse> getAllProductsEnabled(int skip, int limit);

    @Aggregation(pipeline = {
            "{ $project: { _id: 1, name: 1, description: 1, code: 1, price: { $min: '$sizeList.price' }, imageUrl: '$image.url', thumbnail: '$thumbnail.url', status: 1, } } "
    })
    List<GetAllProductsResponse> getAllProducts(int skip, int limit);

//    @Aggregation(pipeline = {
//            "{$match: {_id: ?0, enabled: true}}",
//            "{$project: {_id: 1, name: 1, sizeList: 1, toppingList: 1, description: 1, image: '$image.url'}}"
//    })
    @Query(value = "{$and : [{_id: ?0, status: {$ne: 'HIDDEN'}}]}", fields = "{_id: 1, name: 1, sizeList: 1, toppingList: 1, description: 1, image: '$image.url', status: 1}}")
    GetProductEnabledByIdResponse getProductEnabledById(String id);
    @Query(value = "{$and : [{_id: ?0}]}", fields = "{_id: 1, code: 1, name: 1, sizeList: 1, toppingList: 1, description: 1, image: 1, categoryId: 1, status: 1}}")
    GetProductByIdResponse getProductDetailsById(String id);

    @Aggregation(pipeline = {
            "{ $match: { status: {$ne: 'HIDDEN'} } }",
            "{ $match: { $or: [{ name: { $regex: ?0, $options: 'i' } }, { description: { $regex: ?0, $options: 'i' } }] } }",
            "{ $project: { _id: 1, name: 1, description: 1, status: 1, price: { $min: '$sizeList.price' }, thumbnail: '$thumbnail.url' } }",
            "{ $skip: ?1 }",
            "{ $limit: ?2 }"
    })
    List<GetAllVisibleProductResponse> searchProductVisible(String key, int skip, int limit);
    @Aggregation(pipeline = {
            "{$match:  { $and: [{_id: { $nin: ?1 }, status: 'AVAILABLE'}]}}",
            "{$project: {_id: 1, status: 1, code: 1, name: 1, price: 1, description: 1, thumbnail: '$thumbnail.url'}}",
            "{$limit: ?0}"
    })
    List<GetAllVisibleProductResponse> getSomeProduct(int quantity, List<ObjectId> excludeId);

    @Aggregation(pipeline = {
            "{$match: {$or: [{name: {$regex: ?0, $options: 'i'}}, {description: {$regex: ?0, $options: 'i'}}]}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
            "{$project: {_id: 1, name: 1, code: 1, status: 1,  description: 1, price: {$min: '$sizeList.price'}, thumbnail: '$thumbnail.url'}}"
    })
    List<GetAllProductsResponse> searchProduct(String key, int skip, int limit);
}
