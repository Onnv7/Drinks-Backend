package com.hcmute.drink.repository;

import com.hcmute.drink.collection.EmployeeCollection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeCollection, String> {
    EmployeeCollection findByUsername(String username);
}
