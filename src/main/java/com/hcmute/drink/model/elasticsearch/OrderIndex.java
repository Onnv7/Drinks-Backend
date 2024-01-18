package com.hcmute.drink.model.elasticsearch;


import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Setting(settingPath = "/config/elasticsearch/order/setting.json")
@Mapping(mappingPath = "/config/elasticsearch/order/mapping.json")
@Document(indexName = "order")
public class OrderIndex {
    @Id
    private String id;
    private String code;
    private String customerName;
    private String customerCode;
    private String phoneNumber;
    private String recipientName;
    private int productQuantity;
    private String productThumbnail;
    private Date timeLastEvent;
    private long total;
    private OrderStatus statusLastEvent;
    private OrderType orderType;
}