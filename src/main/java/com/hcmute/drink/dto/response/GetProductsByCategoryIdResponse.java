package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.enums.ProductStatus;
import lombok.Data;

@Data
public class GetProductsByCategoryIdResponse {
    private String id;
    private String name;
    private String description;
    private double price;

    private String thumbnailUrl;
    private ProductStatus status;
}
