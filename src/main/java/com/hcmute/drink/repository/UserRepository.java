package com.hcmute.drink.repository;

import com.hcmute.drink.collection.UserCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserCollection, String> {
    UserCollection findByEmail(String email);
    Optional<UserCollection> findById(String id);
}
