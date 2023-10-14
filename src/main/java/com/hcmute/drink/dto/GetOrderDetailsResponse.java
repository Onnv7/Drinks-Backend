package com.hcmute.drink.dto;

import com.hcmute.drink.collection.TransactionCollection;
import com.hcmute.drink.collection.embedded.OrderLogEmbedded;
import com.hcmute.drink.collection.embedded.ToppingEmbedded;
import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.enums.OrderType;
import com.hcmute.drink.enums.PaymentStatus;
import com.hcmute.drink.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
@Data
public class GetOrderDetailsResponse {
    private String id;
    private String note;
    private double total;
    private OrderType orderType;

    private Address address;
    private Date createdAt;
    private List<Product> products;

    private Transaction transaction;
    @Data
    private class Transaction {
        private String id;
        private PaymentStatus status;
        private PaymentType paymentType;
        private double totalPaid;
    }

    @Data
    private class User {
        private String id;
        private String firstName;
        private String lastName;
        private String email;
    }

    @Data
    private class Address {
        private String details;
        private String note;
        private String recipientName;
        private String phoneNumber;
    }
    @Data
    private class Product {
        private int quantity;
        private List<Topping> toppings;
        private String size;
        private double price;
        private String note;
        private String id;
        private String name;

        @Data
        public class Topping {
            private String name;
            private double price;
        }

    }


}

