package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import com.hcmute.drink.enums.PaymentType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetOrderListResponse {
    private List<Order> orderList;
    private Integer totalPage;
    @Data
    public static class Order {
        private String id;
        private String code;
        private Date createdAt;
        private OrderType orderType;
        private String customerName;
        private Long total;
        private OrderStatus statusLastEvent;
    }
}
