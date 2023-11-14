package com.hcmute.drink.repository;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import org.bson.types.ObjectId;
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
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventLogs', -1]}, 0]}}}",
            "{$addFields: {'productQuantity': {$size: '$products'}}}",
            "{$match: {orderType: ?2, createdAt: {$gte: ?0, $lte: ?1}, 'lastEventLog.orderStatus': ?3}}",
            "{$lookup: {from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}",
            "{ $unwind: '$user' }",
            "{$group: {_id: '$_id', total: {$first: '$total'}, productQuantity: {$first: '$productQuantity'}, products: {$push: '$products'}, statusLastEvent: {$last: '$lastEventLog.orderStatus'}, timeLastEvent: {$last: '$lastEventLog.time'}, phoneNumber: {$first: '$address.phoneNumber'}, customerName: {$first: {$concat: ['$user.firstName', ' ', '$user.lastName']}}}}",
            "{$unwind: '$products'}",
            "{$lookup: {from: 'product', localField: 'products.productId', foreignField: '_id', as: 'products.productSample'}}",
            "{ $unwind: '$products.productSample' }",
            "{$group: {_id: '$_id', total: {$first: '$total'}, phoneNumber: {$first: '$phoneNumber'}, productQuantity: {$first: '$productQuantity'}, customerName: {$first: '$customerName'}, productName: {$first: '$products.productSample.name'}, productThumbnail: {$first: '$products.productSample.thumbnail.url'}, statusLastEvent: {$first: '$statusLastEvent'}, timeLastEvent: {$first: '$timeLastEvent'}}}",
            "{ $sort: {timeLastEvent: -1 }}",
    })
    List<GetAllOrdersByStatusResponse> getAllByTypeAndStatusInDay(Date from, Date to, OrderType orderType, OrderStatus orderStatus);

    @Aggregation(pipeline = {
            "{$match: {'_id': ?0}}",
            "{$unwind: '$products'}",
            "{$lookup: {from: 'product', localField: 'products.productId', foreignField: '_id', as: 'products.productInfo'}}",
            "{$unwind: '$products.productInfo'}",
            "{$group: {_id: '$_id', userId: { $first: '$userId' }, note: { $first: '$note' }, review: { $first: '$review'}, total: { $first: '$total' }, orderType: { $first: '$orderType' }, eventLogs: { $first: '$eventLogs' }, transactionId: { $first: '$transactionId' }, address: { $first: '$address' }, createdAt: { $first: '$createdAt' }, updatedAt: { $first: '$updatedAt' }, products: { $push: { _id: '$products._id', quantity: '$products.quantity', size: '$products.size', toppings: '$products.toppings', price: '$products.price', note: '$products.note', name: '$products.productInfo.name', _id: '$products.productInfo._id' } } }}",
            "{$lookup: {from: 'transaction', localField: 'transactionId', foreignField: '_id', as: 'transaction'}}",
            "{$unwind: '$transaction'}",
            "{$project: {'userId': 0, 'transactionId': 0}}",
    })
    GetOrderDetailsResponse getOrderDetailsById(String id);
    @Aggregation(pipeline = {
            "{$match: {userId: ?0}}",
            "{$addFields: {'lastEventLog': {$slice: ['$eventLogs', -1]}}}",
            "{$addFields: {'productQuantity': {$size: '$products'}}}",
            "{$match: {'lastEventLog.orderStatus': ?1}}",
            "{$unwind: '$lastEventLog'}",
            "{$unwind: '$products'}",
            "{$lookup: {from: 'product', localField: 'products.productId', foreignField: '_id', as: 'products.productSample'}}",
            "{ $unwind: '$products.productSample' }",
            "{$group: {_id: '$_id', total: {$first: '$total'}, productQuantity: {$first: '$productQuantity'}, orderType: {$first: '$orderType'}, productName: { $addToSet: '$products.productSample.name'}, statusLastEvent: { $last: '$lastEventLog.orderStatus'}, timeLastEvent: { $last: '$lastEventLog.time'}}}",
            "{ $unwind: '$productName' }",
            "{$sort: {'timeLastEvent': -1}}",
    })
    List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserId(ObjectId id, OrderStatus orderStatus);


    @Aggregation(pipeline = {
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventLogs', -1]}, 0]}}}",
            "{$addFields: {'productQuantity': {$size: '$products'}}}",
            "{$match: {'lastEventLog.orderStatus': ?0}}",
            "{$lookup: {from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}",
            "{ $unwind: '$user' }",
            "{$group: {_id: '$_id', total: {$first: '$total'}, orderType: {$first: '$orderType'}, productQuantity: {$first: '$productQuantity'}, products: {$push: '$products'}, statusLastEvent: {$last: '$lastEventLog.orderStatus'}, timeLastEvent: {$last: '$lastEventLog.time'}, phoneNumber: {$first: '$address.phoneNumber'}, customerName: {$first: {$concat: ['$user.firstName', ' ', '$user.lastName']}}}}",
            "{$unwind: '$products'}",
            "{$lookup: {from: 'product', localField: 'products.productId', foreignField: '_id', as: 'products.productSample'}}",
            "{ $unwind: '$products.productSample' }",
            "{$group: {_id: '$_id', total: {$first: '$total'}, orderType: {$first: '$orderType'}, phoneNumber: {$first: '$phoneNumber'}, productQuantity: {$first: '$productQuantity'}, customerName: {$first: '$customerName'}, productName: {$first: '$products.productSample.name'}, productThumbnail: {$first: '$products.productSample.thumbnail.url'}, statusLastEvent: {$first: '$statusLastEvent'}, timeLastEvent: {$first: '$timeLastEvent'}}}",
            "{$sort: {'timeLastEvent': -1}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
    })
    List<GetOrderHistoryPageForEmployeeResponse> getAllOrderHistoryForEmployee(OrderStatus orderStatus, int skip, int limit);

    @Aggregation(pipeline = {
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventLogs', -1]}, 0]}}}",
            "{$match: {$and: [{'lastEventLog.orderStatus': ?0}, {'lastEventLog.time': {$gte: ?1, $lt: ?2}}]}}",
            "{$group: {_id: null, orderQuantity: {$sum: 1}}}"
    })
    GetOrderQuantityByStatusResponse getOrderQuantityByStatus(OrderStatus orderStatus, Date from, Date to);

    @Aggregation(pipeline = {
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventLogs', -1]}, 0]}}}",
            "{ $addFields: {'productQuantity': {$size: '$products'}} }",
            "{$match: {'lastEventLog.orderStatus': ?0}}",
            "{$lookup: {from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}",
            "{ $unwind: '$user' }",
            "{$group: {_id: '$_id', total: {$first: '$total'}, orderType: {$first: '$orderType'}, productQuantity: {$first: '$productQuantity'}, products: {$push: '$products'}, statusLastEvent: {$last: '$lastEventLog.orderStatus'}, timeLastEvent: {$last: '$lastEventLog.time'}, phoneNumber: {$first: '$address.phoneNumber'}, customerName: {$first: {$concat: ['$user.firstName', ' ', '$user.lastName']}}}}",
            "{$unwind: '$products'}",
            "{$lookup: {from: 'product', localField: 'products.productId', foreignField: '_id', as: 'products.productSample'}}",
            "{ $unwind: '$products.productSample' }",
            "{$group: {_id: '$_id', total: {$first: '$total'}, orderType: {$first: '$orderType'}, phoneNumber: {$first: '$phoneNumber'}, productQuantity: {$first: '$productQuantity'}, customerName: {$first: '$customerName'}, productName: {$first: '$products.productSample.name'}, productThumbnail: {$first: '$products.productSample.thumbnail.url'}, statusLastEvent: {$first: '$statusLastEvent'}, timeLastEvent: {$first: '$timeLastEvent'}}}",
            "{$match: {$or: [{ $expr: { $regexMatch: { input: { $toString: \"$_id\" }, regex: ?1, options: 'i' } } }, {customerName: {$regex: ?1, $options: 'i'}}, {phoneNumber: {$regex: ?1, $options: 'i'}}]}}",
            "{$skip: ?2}",
            "{$limit: ?3}",
            "{$sort: {timeLastEvent: -1}}"
    })
    List<GetOrderHistoryPageForEmployeeResponse> searchOrderHistoryForEmployee(OrderStatus orderStatus, String key, int skip, int limit);

    @Aggregation(pipeline = {
            "{ $addFields: { lastEventLog: {$arrayElemAt: [{$slice: ['$eventLogs', -1]}, 0]} } },",
            "{ $match: { 'lastEventLog.orderStatus': 'SUCCEED' } },",
            "{ $unwind: '$products' },",
            "{ $group: { _id: '$products.productId', totalQuantity: { $sum: '$products.quantity' } } },",
            "{ $lookup: { from: 'product', localField: '_id', foreignField: '_id', as: 'product' } },",
            "{ $unwind: '$product' },",
            "{ $group: { _id: '$_id', totalQuantity: { $first: '$totalQuantity' }, name: { $first: '$product.name' }, description: { $first: '$product.description' }, sizeList: { $first: '$product.sizeList' }, thumbnail: { $first: '$product.thumbnail.url' } } },",
            "{ $sort: { totalQuantity: -1 } },",
            "{ $project: { _id: 1, name: 1, price: {$min: '$sizeList.price'}, description: 1, thumbnail: 1 } },",
            "{ $limit: ?0 }"
    })
    List<GetAllProductsEnabledResponse> getTopProductQuantityOrder(int top);


    @Query("{'_id' : ?0}")
    @Update("{$push: {eventLogs: ?1}}")
    void completeOrder(String id, OrderStatus orderStatus);

}
