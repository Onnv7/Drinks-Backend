package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hcmute.drink.enums.OrderType;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class GetOrderDetailsResponse {
    private String id;
    private String code;
    private String note;
    private double total;
    private OrderType orderType;
    private User recipientInfo;

    private Date createdAt;
    private List<Product> products;

    private Transaction transaction;
    private Review review;
    private Date receiveTime;

    @Data
    private static class Review {
        private int rating;
        private String content;
    }
    @Data
    private static class Transaction {
        private String id;
        private PaymentStatus status;
        private PaymentType paymentType;
        private double totalPaid;
    }

    @Data
    private static class User {
        private String details;
        private Double longitude;
        private Double latitude;
        private String note;
        private String recipientName;
        private String phoneNumber;
    }
    
    @Data
    private static class Product {
        private int quantity;
        private List<Topping> toppings;
        private String size;
        private double price;
        private String note;
        private String id;
        private String name;

        @Data
        public static class Topping {
            private String name;
            private double price;
        }

    }


}

