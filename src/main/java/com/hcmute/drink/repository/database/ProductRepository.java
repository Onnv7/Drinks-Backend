package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.dto.response.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<ProductCollection, String> {
    @Aggregation(pipeline = {
            "{$match: {$and: [{categoryId: ?0}, {status: {$ne: 'HIDDEN'}}]}}",
            "{$project: {_id: 1, name: 1, description: 1, status: 1, price: 1, thumbnailUrl: 1}}"
    })
    List<GetProductsByCategoryIdResponse> getProductsByCategoryId(ObjectId categoryId);

    @Aggregation(pipeline = {
            "{$match: { status: {$ne: 'HIDDEN'} }}",
            "{$project: {_id: 1, name: 1, code: 1, description: 1, status: 1, price: 1, thumbnailUrl: 1}}",
            "{$skip: ?0}",
            "{$limit: ?1}",
    })
    List<GetAllVisibleProductResponse> getVisibleProductList(int skip, int limit);

    @Aggregation(pipeline = {
            "{$facet: {products: [{$addFields: {categoryIdString: { $toString: '$categoryId' }}}, {$match: {$and: [{isDeleted: false}, {categoryIdString: {$regex: ?2, $options: 'i'}}, {status: {$regex: ?3, $options: 'i'}}]}}, {$project: {_id: 1, code: 1, name: 1, description: 1, status: 1, price: 1, thumbnailUrl: 1}}, {$skip: 0}, {$limit: 2}], totalCount: [{$addFields: {categoryIdString: { $toString: '$categoryId' }}}, {$match: {$and: [{isDeleted: false}, {categoryIdString: {$regex: ?2, $options: 'i'}}, {status: {$regex: ?3, $options: 'i'}}]}}, {$count: 'total'}]}}",
            "{$project: {totalPage: {$ceil: {$divide: [{$toDouble: {$ifNull: [{$first: '$totalCount.total'}, 0]}}, 2]}}, productList: '$products'}}"
    })
    GetProductListResponse getProductList(int skip, int limit, String categoryIdRegex, String productStatusRegex);

    @Query(value = "{$and : [{_id: ?0, status: {$ne: 'HIDDEN'}}]}", fields = "{_id: 1, name: 1, sizeList: 1, toppingList: 1, description: 1, imageUrl: 1, status: 1}}")
    Optional<GetProductEnabledByIdResponse> getProductEnabledById(String id);
    @Query(value = "{$and : [{_id: ?0}]}", fields = "{_id: 1, code: 1, name: 1, sizeList: 1, toppingList: 1, description: 1, imageUrl: 1, categoryId: 1, status: 1}}")
    Optional<GetProductByIdResponse> getProductDetailsById(String id);

    @Aggregation(pipeline = {
            "{ $match: { status: {$ne: 'HIDDEN'} } }",
            "{ $match: { $or: [{ name: { $regex: ?0, $options: 'i' } }, { description: { $regex: ?0, $options: 'i' } }] } }",
            "{ $project: { _id: 1, name: 1, description: 1, status: 1, price: { $min: '$sizeList.price' }, thumbnail: 1 } }",
            "{ $skip: ?1 }",
            "{ $limit: ?2 }"
    })
    List<GetAllVisibleProductResponse> searchProductVisible(String key, int skip, int limit);
    @Aggregation(pipeline = {
            "{$match:  { $and: [{_id: { $nin: ?1 }, status: 'AVAILABLE'}]}}",
            "{$project: {_id: 1, status: 1, code: 1, name: 1, price: 1, description: 1, thumbnailUrl: 1}}",
            "{$limit: ?0}"
    })
    List<GetAllVisibleProductResponse> getSomeProduct(int quantity, List<ObjectId> excludeId);

    @Aggregation(pipeline = {
            "{$match: {$or: [{name: {$regex: ?0, $options: 'i'}}, {description: {$regex: ?0, $options: 'i'}}]}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
            "{$project: {_id: 1, name: 1, code: 1, status: 1,  description: 1, price: {$min: '$sizeList.price'}, thumbnail: 1}}"
    })
    List<GetProductListResponse> searchProduct(String key, int skip, int limit);

    @Aggregation(pipeline = {
            "{$match:  { isDeleted: false } }",
    })
    List<ProductCollection> getAll();

    @Aggregation(pipeline = {
            "{$match:  { $and: [ { _id: ?0 }, { isDeleted: false }] } }",
    })
    Optional<ProductCollection> getById(String id);
}
