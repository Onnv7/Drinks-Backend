package com.hcmute.drink.dto;

import com.hcmute.drink.enums.OrderType;
import lombok.Data;

@Data
public class GetAllShippingOrdersResponse {
    private String _id;
    private User user;


    private long total;
    private OrderType orderType;

    @Data
    private class User {
        private String _id;
        private String firstName;
        private String lastName;
    }
}
