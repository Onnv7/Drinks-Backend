package com.hcmute.drink.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetAllProductsEnabledResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    @JsonProperty("imageUrl")
    private String image;
}
