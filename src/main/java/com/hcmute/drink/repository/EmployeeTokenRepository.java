package com.hcmute.drink.repository;

import com.hcmute.drink.collection.EmployeeTokenCollection;
import com.hcmute.drink.collection.TokenCollection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EmployeeTokenRepository extends MongoRepository<EmployeeTokenCollection, String> {
    List<EmployeeTokenCollection> findByEmployeeId(String userId);
    TokenCollection findByRefreshToken(String refreshToken);
    void deleteByEmployeeId(ObjectId userId);
}