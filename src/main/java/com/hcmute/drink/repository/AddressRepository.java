package com.hcmute.drink.repository;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.dto.GetAddressByUserIdResponse;
import com.hcmute.drink.dto.GetAddressDetailsByIdResponse;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends MongoRepository<AddressCollection, String> {
    @Aggregation(pipeline = {
            "{$match: {userId: ?0}}",
            "{$project: {_id: 1, details: 1, recipientName: 1, phoneNumber: 1, isDefault: 1}}"
    })
    List<GetAddressByUserIdResponse> getAddressByUserId(ObjectId userId);

    @Query(value = "{'_id': ?0}", fields = "{'createdAt': 0, 'updatedAt': 0, 'userId': 0}")
    GetAddressDetailsByIdResponse getAddressDetailsById(String id);

    List<AddressCollection> findByUserId(ObjectId userId);

}
