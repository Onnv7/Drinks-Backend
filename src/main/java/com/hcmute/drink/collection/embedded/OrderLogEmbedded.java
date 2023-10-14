package com.hcmute.drink.collection.embedded;

import com.hcmute.drink.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
@Builder
public class OrderLogEmbedded {
    private OrderStatus orderStatus;
    private Date time;
    private String description;
    private ObjectId employeeId;
}
