package com.hcmute.drink.repository;

import com.hcmute.drink.collection.TokenCollection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends MongoRepository<TokenCollection, String> {
    List<TokenCollection> findByUserId(String userId);
    TokenCollection findByRefreshToken(String refreshToken);
    void deleteByUserId(ObjectId userId);
}
