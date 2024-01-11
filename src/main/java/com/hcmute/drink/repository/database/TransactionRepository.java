package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.dto.response.GetRevenueByTimeResponse;
import com.hcmute.drink.dto.response.GetRevenueCurrentDateResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<TransactionCollection, String> {

    @Aggregation(pipeline = {
            "{$match: {status: 'PAID'}}",
            "{$addFields: {dateField: {$switch: {branches: [{case: {$eq: [?0, 'day']}, then: {year: {$year: '$updatedAt'}, month: {$month: '$updatedAt'}, day: {$dayOfMonth: '$updatedAt'}}}, {case: {$eq: [?0, 'month']}, then: {year: {$year: '$updatedAt'}, month: {$month: '$updatedAt'}, day: 1}}, {case: {$eq: [?0, 'year']}, then: {year: {$year: '$updatedAt'}, month: 1, day: 1}}], default: null}}}}",
            "{$group: {_id: '$dateField', totalPaid: {$sum: '$totalPaid'}}}",
            "{$sort: {'_id.year': 1, '_id.month': 1, '_id.day': 1}}",
            "{$project: {_id: 0, time: '$_id', revenue: '$totalPaid'}}"
    })
    List<GetRevenueByTimeResponse> getRevenueByTime(String time);

    @Aggregation(pipeline = {
            "{$match: {status: 'PAID', createdAt: {$gte: ?0, $lt: ?1}}}",
            "{$group: {_id: null, totalPaid: {$sum: '$totalPaid'}}}",
            "{$project: {_id: 0, revenue: '$totalPaid'}}",
            "{$limit: 1}"
    })
    GetRevenueCurrentDateResponse getRevenueCurrentDate(Date from, Date to);
}
