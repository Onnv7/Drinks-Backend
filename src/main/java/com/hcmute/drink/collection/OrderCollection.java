package com.hcmute.drink.collection;


import com.hcmute.drink.collection.embedded.*;
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
    public static final String PREFIX_CODE_SHIPPING = "OS";
    @Transient
    public static final String PREFIX_CODE_ONSITE = "OA";
    @Transient
    public static final int LENGTH_NUMBER = 8;

    @Id
    private String id;
    private ObjectId userId;
    @Indexed(unique = true)
    private String code;

    private List<OrderItemEmbedded> itemList;
    private String note;

    private Long shippingFee;
    private Long total;
    private OrderType orderType;

    private String orderCouponCode;
    private Long orderDiscount;

    private String shippingCouponCode;
    private Long shippingDiscount;

    private List<OrderEventEmbedded> eventList;

    @Builder.Default
    private ReviewEmbedded review = null;

    private ObjectId transactionId;
    private RecipientInfoEmbedded recipientInfo;
    private BranchEmbedded branch;
    private Date receiveTime;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
