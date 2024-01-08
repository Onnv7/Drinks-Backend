package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.SizeModel;
import com.hcmute.drink.common.ToppingModel;
import lombok.Data;

import java.util.List;

@Data
public class GetProductsByCategoryIdResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    @JsonProperty("thumbnailUrl")
    private String thumbnail;
}
