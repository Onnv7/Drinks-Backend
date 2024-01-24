package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.enums.ProductStatus;
import lombok.Data;

import java.util.List;

@Data
public class GetProductListResponse {
    private int totalPage;
    private List<Product> productList;

    @Data
    public static class Product {
        private String id;
        private String code;
        private String name;
        private double price;
        private String thumbnailUrl;
        private ProductStatus status;
    }
}
