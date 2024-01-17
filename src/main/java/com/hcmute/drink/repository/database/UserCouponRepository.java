package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.UserCouponCollection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponRepository extends MongoRepository<UserCouponCollection, String> {
    Optional<UserCouponCollection> findByUserIdAndCouponCode(ObjectId userId, String couponCode);
}
