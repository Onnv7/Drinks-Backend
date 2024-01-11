package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.dto.response.GetAllCategoriesWithoutDisabledResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository  extends MongoRepository<CategoryCollection, String> {
    Optional<CategoryCollection> findByName(String name);

    @Aggregation(pipeline = {
        "{$match: {enabled: true}}",
        "{$project: {'enabled': 0, 'createdAt': 0, 'updatedAt': 0}}"
    })
    List<GetAllCategoriesWithoutDisabledResponse> getAllCategoriesWithoutDisabled();
}
