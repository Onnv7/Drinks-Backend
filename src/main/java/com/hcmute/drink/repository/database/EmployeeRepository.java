package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.dto.response.GetAllEmployeeResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeCollection, String> {
    Optional<EmployeeCollection> findByUsername(String username);

    @Aggregation(pipeline = {
            "{$facet: {employeeList: [{$project: {_id: 1, code: 1, firstName: 1, lastName: 1, username: 1, birthDate: 1, gender: 1, status: 1}}, {$skip: ?0}, {$limit: ?1}], totalCount: [{$count: 'total'}]}}",
            "{$project: {totalPage: {$ceil: {$divide: [{$toDouble: {$ifNull: [{$first: '$totalCount.total'}, 0]}}, ?1]}}, employeeList: '$employeeList'}}"
    })
    List<GetAllEmployeeResponse> getAllEmployees(int skip, int limit);

    @Aggregation(pipeline = {
            "{$facet: {employeeList: [{$match: {$or: [{code: {$regex: ?0, $options: 'i'}}, {username: {$regex: ?0, $options: 'i'}}]}}, {$project: {_id: 1, code: 1, firstName: 1, lastName: 1, username: 1, birthDate: 1, gender: 1, status: 1}}, {$skip: ?1}, {$limit: ?2}], totalCount: [{$match: {$or: [{code: {$regex: ?0, $options: 'i'}}, {username: {$regex: ?0, $options: 'i'}}]}}, {$count: 'total'}, {$limit: 1} ]}}",
            "{$project: {totalPage: {$ceil: {$divide: [{$toDouble: {$ifNull: [{$first: '$totalCount.total'}, 0]}}, ?2]}}, employeeList: '$employeeList'}}"
    })
    List<GetAllEmployeeResponse> searchEmployee(String key, int skip, int limit);
}
