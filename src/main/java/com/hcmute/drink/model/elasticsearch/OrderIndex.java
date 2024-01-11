package com.hcmute.drink.model.elasticsearch;


import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(indexName = "order")
public class OrderIndex {
    private String id;
    private String code;
    private String customerName;
    private String customerCode;
    private String phoneNumber;
    private String recipientName;
}
