package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.BannerCollection;
import com.hcmute.drink.dto.response.GetBannerDetailsResponse;
import com.hcmute.drink.dto.response.GetBannerListResponse;
import com.hcmute.drink.dto.response.GetVisibleBannerListResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BannerRepository extends MongoRepository<BannerCollection, String> {

    @Aggregation(pipeline = {
            "{$match: { $and: [  {_id: ?0}, {isDeleted: false} ] }}",
            "{ $project: {_id: 1, status: 1, name: 1, imageUrl: 1 } }"
    })
    Optional<GetBannerDetailsResponse> getBannerDetailsById(String id);

    @Aggregation(pipeline = {
            "{$match: {isDeleted: false}}",
    })
    List<BannerCollection> getAll();


    @Aggregation(pipeline = {
            "{$match: { $and: [  {_id: ?0}, {isDeleted: false} ] }}",
    })
    Optional<BannerCollection> getById(String id);

    @Aggregation(pipeline = {
            "{$match: {isDeleted: false}}",
            "{ $project: { _id: 0, imageUrl: 1 } }"
    })
    List<GetVisibleBannerListResponse> getVisibleBannerList();
}
