package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.CouponCollection;
import com.hcmute.drink.dto.response.GetCouponDetailsByIdResponse;
import com.hcmute.drink.dto.response.GetCouponListResponse;
import com.hcmute.drink.dto.response.GetReleaseCouponByIdResponse;
import com.hcmute.drink.dto.response.GetReleaseCouponListResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends MongoRepository<CouponCollection, String> {
    @Aggregation(pipeline = {
            "{$match: { $and: [ {_id: ?0}, {isDeleted: false} ] }}",
    })
    Optional<CouponCollection> getById(String id);

    @Aggregation(pipeline = {
            "{$match: { $and: [ {code: ?0}, {isDeleted: false} ] }}",
    })
    Optional<CouponCollection> getByCode(String code);

    @Aggregation(pipeline = {
            "{$match: {$and: [{status: 'RELEASE'}, {isDeleted: false}]}}",
            "{$project: {_id: 1, description: 1, startDate: 1, expirationDate: 1}}"
    })
    List<GetReleaseCouponListResponse> getReleaseCouponList();

    @Aggregation(pipeline = {
            "{$match: {$and: [{_id: ?0}, {status: 'RELEASE'}, {isDeleted: false}]}}",
            "{$project: {_id: 1, description: 1, code: 1, startDate: 1, expirationDate: 1, 'conditionList.description': 1}}",
            "{$unwind: '$conditionList'}",
            "{$group: {_id: '$_id', conditionList: {$push: '$conditionList.description'}, code: {$first: '$code'}, description: {$first: '$description'}, startDate: {$first: '$startDate'}, expirationDate: {$first: '$expirationDate'}}}"
    })
    List<GetReleaseCouponByIdResponse> getReleaseCouponById(String couponId);

    @Aggregation(pipeline = {
            "{$match: {isDeleted: false}}",
            "{$addFields: {isExpired: {$cond: {if: {$lt: ['$expirationDate', new Date()]}, then: true, else: false}}}}"
    })
    List<GetCouponListResponse> getCouponList();

    @Aggregation(pipeline = {
            "{$match: { $and: [{ _id: ?0 }, { isDeleted: false }]}}",
    })
    GetCouponDetailsByIdResponse getCouponById(String couponId);
}
