package com.hcmute.drink.repository;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.dto.GetAllOrderHistoryByUserIdResponse;
import com.hcmute.drink.dto.GetAllShippingOrdersResponse;
import com.hcmute.drink.dto.GetOrderDetailsResponse;
import com.hcmute.drink.enums.OrderStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<OrderCollection, String> {
    OrderCollection findByTransactionId(ObjectId transactionId);

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
            "{$unwind: '$products'}",
            "{$lookup: {from: 'product', localField: 'products.productId', foreignField: '_id', as: 'products.productInfo'}}",
            "{$unwind: '$products.productInfo'}",
            "{$group: {_id: '$_id', userId: { $first: '$userId' }, note: { $first: '$note' }, total: { $first: '$total' }, orderType: { $first: '$orderType' }, eventLogs: { $first: '$eventLogs' }, transactionId: { $first: '$transactionId' }, address: { $first: '$address' }, createdAt: { $first: '$createdAt' }, updatedAt: { $first: '$updatedAt' }, products: { $push: { _id: '$products._id', quantity: '$products.quantity', size: '$products.size', toppings: '$products.toppings', price: '$products.price', note: '$products.note', name: '$products.productInfo.name', _id: '$products.productInfo._id' } } }}",
            "{$lookup: {from: 'transaction', localField: 'transactionId', foreignField: '_id', as: 'transaction'}}",
            "{$unwind: '$transaction'}",
            "{$project: {'userId': 0, 'transactionId': 0}}"
    })
    GetOrderDetailsResponse getOrderDetailsById(String id);
    @Aggregation(pipeline = {
            "{$match: {userId: ?0}}",
            "{$addFields: {'lastEventLog': {$slice: ['$eventLogs', -1]}}}",
            "{$match: {'lastEventLog.orderStatus': ?1}}",
            "{$unwind: '$products'}",
            "{$lookup: {from: 'product', localField: 'products.productId', foreignField: '_id', as: 'products.productInfo'}}",
            "{$unwind: '$products.productInfo'}",
            "{$group: {_id: '$_id', userId: {$first: '$userId'}, note: {$first: '$note'}, total: {$first: '$total'}, orderType: {$first: '$orderType'}, eventLogs: {$first: '$eventLogs'}, transactionId: {$first: '$transactionId'}, address: {$first: '$address'}, createdAt: {$first: '$createdAt'}, updatedAt: {$first: '$updatedAt'}, totalQuantity: {$sum: '$products.quantity'}, productInfo: {$first: {name: '$products.productInfo.name', image: {$arrayElemAt: ['$products.productInfo.imageList', 0]}}}}}}",
            "{$project: {userId: 0, note: 0, eventLogs: 0, transactionId: 0, address: 0, updatedAt: 0}}"
    })
    List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserId(String id, OrderStatus orderStatus);


    @Query("{'_id' : ?0}")
    @Update("{$push: {eventLogs: ?1}}")
    void completeOrder(String id, OrderStatus orderStatus);

}
