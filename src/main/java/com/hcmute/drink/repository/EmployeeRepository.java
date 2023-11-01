package com.hcmute.drink.repository;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.dto.GetAllEmployeeResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeCollection, String> {
    EmployeeCollection findByUsername(String username);

    @Query(value = "{}", fields = "{'_id': 1, 'firstName': 1, 'lastName': 1, 'username': 1, 'birthDate': 1, 'gender': 1, 'enabled': 1}")
    List<GetAllEmployeeResponse> getAllEmployees();
}
