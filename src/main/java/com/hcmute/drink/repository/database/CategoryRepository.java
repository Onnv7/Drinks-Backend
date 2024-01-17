package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.dto.response.GetVisibleCategoryListResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository  extends MongoRepository<CategoryCollection, String> {

    @Aggregation(pipeline = {
        "{$match: {status: 'VISIBLE'}}",
        "{$project: {'status': 0, 'createdAt': 0, 'updatedAt': 0}}"
    })
    List<GetVisibleCategoryListResponse> getVisibleCategoryList();

    @Aggregation(pipeline = {
            "{$match: {isDeleted: false}}",
    })
    List<CategoryCollection> getAll();

    @Aggregation(pipeline = {
            "{$match: { $and: [ {_id: ?0}, {isDeleted: false} ] }}",
    })
    Optional<CategoryCollection> getById(String id);

    @Aggregation(pipeline = {
            "{$match: { $and: [ {isDeleted: false}, {name: ?0} ] }}",
    })
    Optional<CategoryCollection> getByName(String name);
}
