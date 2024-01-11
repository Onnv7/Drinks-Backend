package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.ConfirmationCollection;
import com.hcmute.drink.collection.UserCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends MongoRepository<ConfirmationCollection, String> {
    ConfirmationCollection findByEmail(String email);
}
