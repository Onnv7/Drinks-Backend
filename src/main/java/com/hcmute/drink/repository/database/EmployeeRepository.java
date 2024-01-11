package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.dto.response.GetAllEmployeeResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeCollection, String> {
    Optional<EmployeeCollection> findByUsername(String username);

    @Query(value = "{}", fields = "{'_id': 1, 'code': 1, 'firstName': 1, 'lastName': 1, 'username': 1, 'birthDate': 1, 'gender': 1, 'enabled': 1}")
    List<GetAllEmployeeResponse> getAllEmployees();
}
