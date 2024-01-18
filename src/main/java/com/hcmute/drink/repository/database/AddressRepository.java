package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.dto.database.GetUserIdFromAddressDto;
import com.hcmute.drink.dto.response.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends MongoRepository<AddressCollection, String> {
    @Aggregation(pipeline = {
            "{$match: {userId: ?0}}",
            "{$project: {_id: 1, details: 1, recipientName: 1, phoneNumber: 1, isDefault: 1, longitude: 1, latitude: 1}}"
    })
    List<GetAddressListByUserIdResponse> getAddressListByUserId(ObjectId userId);

    @Query(value = "{'_id': ?0}", fields = "{'createdAt': 0, 'updatedAt': 0, 'userId': 0}")
    GetAddressDetailsByIdResponse getAddressDetailsById(String id);

    List<AddressCollection> findByUserId(ObjectId userId);

    @Aggregation(pipeline = {
            "{$match: {_id: ?0}}",
            "{$lookup: {from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}",
            "{$project: {_id: 0, userId: '$user._id'}}",
            "{$unwind: '$userId'}"
    })
    Optional<GetUserIdFromAddressDto> getUserIdFromAddressId(String addressId);

}
