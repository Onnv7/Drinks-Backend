package com.hcmute.drink.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetAllProductsResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    @JsonProperty("thumbnailUrl")
    private String thumbnail;
    @JsonProperty("imageUrl")
    private String image;
    private boolean enabled;
}
