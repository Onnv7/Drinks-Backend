package com.hcmute.drink.repository.database;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.OrderStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<OrderCollection, String> {
    Optional<OrderCollection> findByTransactionId(ObjectId transactionId);

    @Aggregation(pipeline = {
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventList', -1]}, 0]}}}",
            "{$addFields: {'productQuantity': {$size: '$itemList'}}}",
            "{$match: {'branch._id': ?0,orderType: 'SHIPPING', createdAt: {$gte: ?1, $lte: ?2}, 'lastEventLog.orderStatus': ?5}}",
            "{$lookup: {from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}",
            "{$unwind: '$user'}",
            "{$group: {_id: '$_id', total: {$first: '$total'}, productQuantity: {$first: '$productQuantity'}, itemList: {$push: '$itemList'}, statusLastEvent: {$last: '$lastEventLog.orderStatus'}, timeLastEvent: {$last: '$lastEventLog.time'}, phoneNumber: {$first: '$address.phoneNumber'}, customerName: {$first: {$concat: ['$user.firstName', ' ', '$user.lastName']}}}}",
            "{$unwind: '$itemList'}",
            "{$lookup: {from: 'product', localField: 'itemList.productId', foreignField: '_id', as: 'itemList.productSample'}}",
            "{$unwind: '$itemList.productSample'}",
            "{$group: {_id: '$_id', total: {$first: '$total'}, phoneNumber: {$first: '$phoneNumber'}, productQuantity: {$first: '$productQuantity'}, customerName: {$first: '$customerName'}, productName: {$first: '$itemList.productSample.name'}, productThumbnail: {$first: '$itemList.productSample.thumbnailUrl'}, statusLastEvent: {$first: '$statusLastEvent'}, timeLastEvent: {$first: '$timeLastEvent'}}}",
            "{$sort: {timeLastEvent: 1}}",
            "{$skip: ?3}",
            "{$limit: ?4}"
    })
    List<GetShippingOrderQueueResponse> getShippingOrderQueueToday(String branchId, Date from, Date to, int skip, int limit, OrderStatus orderStatus);

    @Aggregation(pipeline = {
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventList', -1]}, 0]}}}",
            "{$addFields: {'productQuantity': {$size: '$itemList'}}}",
            "{$match: {'branch._id': ?0, orderType: 'ONSITE', createdAt: {$gte: ?1, $lte: ?2}, 'lastEventLog.orderStatus': ?5}}",
            "{$lookup: {from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}",
            "{$unwind: '$user'}",
            "{$group: {_id: '$_id', total: {$first: '$total'}, receiveTime: {$first: '$receiveTime'}, productQuantity: {$first: '$productQuantity'}, itemList: {$push: '$itemList'}, statusLastEvent: {$last: '$lastEventLog.orderStatus'}, timeLastEvent: {$last: '$lastEventLog.time'}, phoneNumber: {$first: '$user.phoneNumber'}, customerName: {$first: {$concat: ['$user.firstName', ' ', '$user.lastName']}}}}",
            "{$unwind: '$itemList'}",
            "{$lookup: {from: 'product', localField: 'itemList.productId', foreignField: '_id', as: 'itemList.productSample'}}",
            "{$unwind: '$itemList.productSample'}",
            "{$group: {_id: '$_id', total: {$first: '$total'}, receiveTime: {$first: '$receiveTime'}, phoneNumber: {$first: '$phoneNumber'}, productQuantity: {$first: '$productQuantity'}, customerName: {$first: '$customerName'}, productName: {$first: '$itemList.productSample.name'}, productThumbnail: {$first: '$itemList.productSample.thumbnailUrl'}, statusLastEvent: {$first: '$statusLastEvent'}, timeLastEvent: {$first: '$timeLastEvent'}}}",
            "{$sort: {timeLastEvent: 1}}",
            "{$skip: ?3}",
            "{$limit: ?4}"
    })
    List<GetOnsiteOrderQueueResponse> getOnsiteOrderQueueToday(String branchId, Date from, Date to, int skip, int limit, OrderStatus orderStatus);

    @Aggregation(pipeline = {
            "{$match: {'_id': ?0}}",
            "{$unwind: '$itemList'}",
            "{$lookup: {from: 'product', localField: 'itemList.productId', foreignField: '_id', as: 'itemList.productInfo'}}",
            "{$unwind: '$itemList.productInfo'}",
            "{$group: {_id: '$_id', userId: {$first: '$userId'}, code: {$first: 'code'}, shippingFee: {$first: '$shippingFee'}, shippingDiscount: {$first: '$shippingDiscount'}, orderDiscount: {$first: '$orderDiscount'}, receiveTime: {$first: '$receiveTime'}, note: {$first: '$note'}, branch: {$first: '$branch'}, review: {$first: '$review'}, total: {$first: '$total'}, orderType: {$first: '$orderType'}, eventList: {$first: '$eventList'}, transactionId: {$first: '$transactionId'}, recipientInfo: {$first: '$recipientInfo'}, createdAt: {$first: '$createdAt'}, updatedAt: {$first: '$updatedAt'}, itemList: {$push: {productGift: '$itemList.productGift', moneyDiscount: '$itemList.moneyDiscount', quantity: '$itemList.quantity', discount: '$itemList.discount', size: '$itemList.size', toppings: '$itemList.toppings', price: '$itemList.price', note: '$itemList.note', name: '$itemList.productInfo.name', _id: '$itemList.productInfo._id'}}}}",
            "{$lookup: {from: 'transaction', localField: 'transactionId', foreignField: '_id', as: 'transaction'}}",
            "{$unwind: '$transaction'}",
            "{$project: {transactionId: 0}}"
    })
    GetOrderByIdResponse getOrderDetailsById(String id);

    @Aggregation(pipeline = {
            "{$match: {userId: ?0}}",
            "{$addFields: {'lastEventLog': {$slice: ['$eventList', -1]}}}",
            "{$addFields: {'productQuantity': {$size: '$itemList'}}}",
            "{$match: {'lastEventLog.orderStatus': ?1}}",
            "{$unwind: '$lastEventLog'}",
            "{$unwind: '$itemList'}",
            "{$lookup: {from: 'product', localField: 'itemList.productId', foreignField: '_id', as: 'itemList.productSample'}}",
            "{ $unwind: '$itemList.productSample' }",
            "{$group: {_id: '$_id', code: {$first: '$code'}, total: {$first: '$total'}, productQuantity: {$first: '$productQuantity'}, orderType: {$first: '$orderType'}, productName: { $first: '$itemList.productSample.name'}, statusLastEvent: { $last: '$lastEventLog.orderStatus'}, timeLastEvent: { $last: '$lastEventLog.time'}}}",
            "{$unwind: '$productName' }",
            "{$sort: {'timeLastEvent': -1}}",
            "{$skip: ?2}",
            "{$limit: ?3}",
    })
    List<GetAllOrderHistoryByUserIdResponse> getOrdersHistoryByUserIdAndOrderStatus(ObjectId id, OrderStatus orderStatus, int skip, int limit);


    @Aggregation(pipeline = {
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventList', -1]}, 0]}}}",
            "{$addFields: {'productQuantity': {$size: '$itemList'}}}",
            "{$match: {'lastEventLog.orderStatus': ?0}}",
            "{$lookup: {from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}",
            "{ $unwind: '$user' }",
            "{$group: {_id: '$_id', total: {$first: '$total'}, code: {$first: '$code'}, orderType: {$first: '$orderType'}, productQuantity: {$first: '$productQuantity'}, itemList: {$push: '$itemList'}, statusLastEvent: {$last: '$lastEventLog.orderStatus'}, timeLastEvent: {$last: '$lastEventLog.time'}, phoneNumber: {$first: '$address.phoneNumber'}, customerName: {$first: {$concat: ['$user.firstName', ' ', '$user.lastName']}}}}",
            "{$unwind: '$itemList'}",
            "{$lookup: {from: 'product', localField: 'itemList.productId', foreignField: '_id', as: 'itemList.productSample'}}",
            "{ $unwind: '$itemList.productSample' }",
            "{$group: {_id: '$_id', total: {$first: '$total'}, code: {$first: '$code'}, orderType: {$first: '$orderType'},  phoneNumber: {$first: '$phoneNumber'}, productQuantity: {$first: '$productQuantity'}, customerName: {$first: '$customerName'}, productName: {$first: '$itemList.productSample.name'}, productThumbnail: {$first: '$itemList.productSample.thumbnailUrl'}, statusLastEvent: {$first: '$statusLastEvent'}, timeLastEvent: {$first: '$timeLastEvent'}}}",
            "{$sort: {'timeLastEvent': -1}}",
            "{$skip: ?1}",
            "{$limit: ?2}",
    })
    List<GetOrderHistoryForEmployeeResponse> getAllOrderHistoryForEmployee(OrderStatus orderStatus, int skip, int limit);

    @Aggregation(pipeline = {
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventList', -1]}, 0]}}}",
            "{$match: {$and: [{'lastEventLog.orderStatus': ?0}, {'lastEventLog.time': {$gte: ?1, $lt: ?2}}]}}",
            "{$group: {_id: null, orderQuantity: {$sum: 1}}}"
    })
    GetOrderQuantityByStatusResponse getOrderQuantityByStatus(OrderStatus orderStatus, Date from, Date to);


    @Aggregation(pipeline = {
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventList', -1]}, 0]}}}",
            "{$match: {'lastEventLog.orderStatus': 'SUCCEED'}}",
            "{$unwind: '$itemList'}",
            "{$group: {_id: '$itemList.productId', totalQuantity: { $sum: '$itemList.quantity' }}}",
            "{$lookup: {from: 'product', localField: '_id', foreignField: '_id', as: 'product'}}",
            "{$unwind: '$product'}",
            "{$group: {_id: '$_id', totalQuantity: { $first: '$totalQuantity' }, price: { $first: '$product.price' }, code: { $first: '$product.code' }, status: { $first: '$product.status' }, name: { $first: '$product.name' }, description: { $first: '$product.description' }, sizeList: { $first: '$product.sizeList' }, thumbnailUrl: { $first: '$product.thumbnailUrl' } }}",
            "{$match: {'status': 'AVAILABLE'}}",
            "{$sort: { totalQuantity: -1 }}",
            "{$project: {_id: 1, price: 1, code: 1, status: 1, name: 1, description: 1, thumbnailUrl: 1}}",
            "{$limit: ?0}"
    })
    List<GetAllVisibleProductResponse> getTopProductQuantityOrder(int top);

    @Aggregation(pipeline = {
            "{$addFields: {lastEventLog: {$arrayElemAt: [{$slice: ['$eventList', -1]}, 0]}}}",
            "{$addFields: {productQuantity: {$size: '$itemList'}}}",
            "{$match: {orderType: 'SHIPPING', createdAt: {$gte: ?0, $lte: ?1}, 'lastEventLog.orderStatus': ?2}}"
    })
    List<OrderCollection> getShippingOrdersByStatusLastAndDateTimeCreated(Date from, Date to, OrderStatus orderStatus);

    @Aggregation(pipeline = {
            "{$facet: {orderList: [{$addFields: {'lastEventLog': {$slice: ['$eventList', -1]}}}, {$unwind: '$lastEventLog'}, {$match: {'lastEventLog.orderStatus': {$regex: ?2, $options: 'i'}}}, {$lookup: { from: 'user', localField: 'userId', foreignField: '_id', as: 'user'}}, {$unwind: '$user'}, {$project: {_id: 1, code: 1, createdAt: 1, total: 1, orderType: 1, statusLastEvent: '$lastEventLog.orderStatus', customerName: {$concat: ['$user.firstName', ' ', '$user.lastName']}}}, {$sort: {createdAt: -1}}, {$skip: ?0}, {$limit: ?1}], totalCount: [{$addFields: {'lastEventLog': {$slice: ['$eventList', -1]}}}, {$unwind: '$lastEventLog'}, {$match: {'lastEventLog.orderStatus': {$regex: ?2, $options: 'i'}}}, {$count: 'total'}]}}",
            "{$project: {totalPage: {$ceil: {$divide: [{$toDouble: {$ifNull: [{$first: '$totalCount.total'}, 0]}}, ?1]}}, orderList: '$orderList'}}"
    })
    GetOrderListResponse getOrderListForAdmin(int skip, int limit, String status);
}
