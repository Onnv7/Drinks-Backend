package com.hcmute.drink.collection;


import com.hcmute.drink.common.OrderDetailsModel;
import com.hcmute.drink.common.OrderLogModel;
import com.hcmute.drink.common.ReviewModel;
import com.hcmute.drink.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "order")
public class OrderCollection {
    @Id
    private String id;
    private ObjectId userId;

    private List<OrderDetailsModel> products;
    private String note;

    private long total;
    private OrderType orderType;

    private List<OrderLogModel> eventLogs;


    private ReviewModel review;

    private ObjectId transactionId;
    private AddressCollection address;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
