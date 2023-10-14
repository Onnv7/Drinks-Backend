package com.hcmute.drink.repository;

import com.hcmute.drink.collection.TokenCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<TokenCollection, String> {
}
