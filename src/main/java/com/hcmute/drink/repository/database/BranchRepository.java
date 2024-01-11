package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.BranchCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends MongoRepository<BranchCollection, String> {
}
