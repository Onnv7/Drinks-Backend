package com.hcmute.drink.collection;


import com.hcmute.drink.collection.embedded.OrderDetailsEmbedded;
import com.hcmute.drink.collection.embedded.OrderLogEmbedded;
import com.hcmute.drink.collection.embedded.ReviewEmbedded;
import com.hcmute.drink.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "order")
public class OrderCollection {

    @Transient
    public static final String SEQUENCE_NAME = "order_sequence";
    @Transient
    public static final String PREFIX_CODE = "OB";
    @Transient
    public static final int LENGTH_NUMBER = 8;

    @Id
    private String id;
    private ObjectId userId;
    @Indexed(unique = true)
    private String code;

    private List<OrderDetailsEmbedded> products;
    private String note;

    private long total;
    private OrderType orderType;

    private List<OrderLogEmbedded> eventLogs;

    @Builder.Default
    private ReviewEmbedded review = null;

    private ObjectId transactionId;
    private AddressCollection address;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
