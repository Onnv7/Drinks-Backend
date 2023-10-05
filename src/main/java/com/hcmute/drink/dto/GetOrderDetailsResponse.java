package com.hcmute.drink.dto;

import com.hcmute.drink.collection.embedded.ToppingEmbedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class GetOrderDetailsResponse {
    private String _id;
    private User user;
    private String note;
    private double total;
    private Transaction transaction;
    private List<Product> products;




    @Data
    private class Topping {
        private String name;
        private double price;
    }
    @Data
    private class Product {
        private String name;
        private String size;
        private double price;
        private int quantity;
        private String note;
        private List<Topping> toppings;
    }

    @Data
    private class User {
        private String firstName;
        private String lastName;
    }
    @Data
    private class Transaction {
        private String _id;
        private String status;
        private String paymentType;
        private String totalPaid;
    }
}
