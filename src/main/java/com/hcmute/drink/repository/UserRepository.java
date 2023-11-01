package com.hcmute.drink.repository;

import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.dto.GetAllUserResponse;
import com.hcmute.drink.dto.GetUserByIdResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserCollection, String> {
    UserCollection findByEmail(String email);
    Optional<UserCollection> findById(String id);

    @Aggregation(pipeline = {
            "{$project: {password: 0, createdAt: 0, roles: 0}}"
    })
    List<GetAllUserResponse> getAllUsers() ;

    @Aggregation(pipeline = {
            "{$match: {_id: ?0}}",
            "{$project: {password: 0, createdAt: 0, updatedAt: 0, roles: 0, enabled: 0}}"
    })
    GetUserByIdResponse getUserProfileById(String id);
}
