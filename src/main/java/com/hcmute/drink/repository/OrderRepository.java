package com.hcmute.drink.repository;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.dto.GetAllShippingOrdersResponse;
import com.hcmute.drink.dto.GetOrderDetailsResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<OrderCollection, String> {
    OrderCollection findByTransactionId(String transactionId);

    @Aggregation(pipeline = {
            "{$match: {'createdAt': {$gte: ?0, $lt: ?1}}}",
            "{$match: {'orderType': 'SHIPPING'}}",
            "{$addFields: {'lastEventLog': { $slice: ['$eventLogs', -1] }}}",
            "{$match: {'lastEventLog.orderStatus': 'CREATED'}}",
            "{$lookup: {from: 'transaction', localField: 'transactionId', foreignField: '_id', as: 'transaction'}}",
            "{$unwind: '$transaction'}",
            "{$match: {$or:[{'transaction.status': 'PAID', 'transaction.paymentType': 'BANKING'}, {'transaction.paymentType': 'CASHING'}]}}",
            "{$lookup: {from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}",
            "{$unwind: '$user'}",
            "{$project: {user: 1, total: 1, orderType: 1}}"
    })
    List<GetAllShippingOrdersResponse> getAllShippingOrdersQueueForEmployee(Date from, Date to);

    @Aggregation(pipeline = {
            "{$match: {'_id': ?0}}",
            "{$lookup: {from: 'transaction', localField: 'transactionId', foreignField: '_id', as: 'transaction'}}",
            "{$unwind: '$transaction'}",
            "{$lookup: {from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}",
            "{$unwind: '$user'}",
//            "{$lookup: {from: 'product', localField: 'products.productId', foreignField: '_id', as: 'products'}}",
//            "{$unwind: '$products'}",
    })
    GetOrderDetailsResponse getOrderDetailsById(String id);
}
