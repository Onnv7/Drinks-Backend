package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.SequenceCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceRepository extends MongoRepository<SequenceCollection, String> {
}
