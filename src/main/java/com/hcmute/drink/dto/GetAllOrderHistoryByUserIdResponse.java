package com.hcmute.drink.dto;

import com.hcmute.drink.enums.OrderType;
import lombok.Data;

@Data
public class GetAllOrderHistoryByUserIdResponse {
    private String id;
    private double total;
    private OrderType orderType;
    private int totalQuantity;
    private Product productInfo;

    @Data
    private class Product {
        private String name;
        private Image image;

        @Data
        private class Image {
            private String _id;
            private String url;
        }
    }
}
