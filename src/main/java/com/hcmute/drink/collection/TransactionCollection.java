package com.hcmute.drink.collection;

import com.hcmute.drink.common.OrderDetailsModel;
import com.hcmute.drink.common.OrderStatus;
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
@Document(collection = "transaction")
public class TransactionCollection {
    @Id
    private String id;
    private ObjectId userId;
    private List<OrderDetailsModel> products;
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;
    private String note;

    private double total;



    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
