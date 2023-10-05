package com.hcmute.drink.repository;

import com.hcmute.drink.collection.AddressCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends MongoRepository<AddressCollection, String> {
}
