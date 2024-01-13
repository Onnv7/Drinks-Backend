package com.hcmute.drink.collection.embedded;

import com.hcmute.drink.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
@Builder
public class OrderEventEmbedded {
    private OrderStatus orderStatus;
    private Date time;
    private String description;
    private ObjectId makerId;
    private boolean isEmployee;
}
