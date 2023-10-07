package com.hcmute.drink.dto;

import com.hcmute.drink.collection.AddressCollection;
import com.hcmute.drink.collection.embedded.OrderDetailsEmbedded;
import com.hcmute.drink.collection.embedded.OrderLogEmbedded;
import com.hcmute.drink.collection.embedded.ReviewEmbedded;
import com.hcmute.drink.common.AddressModel;
import com.hcmute.drink.common.OrderDetailsModel;
import com.hcmute.drink.common.OrderLogModel;
import com.hcmute.drink.enums.OrderType;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class UpdateOrderStatusResponse {
    private String id;
    private String userId;

    private List<OrderDetailsModel> products;
    private String note;

    private long total;
    private OrderType orderType;

    private List<OrderLogModel> eventLogs;


    private String transactionId;
    private AddressModel address;
}
