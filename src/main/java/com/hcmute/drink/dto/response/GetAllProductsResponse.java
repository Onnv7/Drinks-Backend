package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.enums.ProductStatus;
import lombok.Data;

import java.util.List;

@Data
public class GetAllProductsResponse {
    private String id;
    private String code;
    private String name;
//    private String description;
    private double price;
    @JsonProperty("thumbnailUrl")
    private String thumbnail;
    private ProductStatus status;
}
