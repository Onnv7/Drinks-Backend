package com.hcmute.drink.dto.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.enums.ProductStatus;
import lombok.Data;

@Data
public class SearchVisibleProduct {
    private String id;
    private String name;
    private String description;
    private double price;
    @JsonProperty("thumbnailUrl")
    private String thumbnail;
    private ProductStatus status;
}
