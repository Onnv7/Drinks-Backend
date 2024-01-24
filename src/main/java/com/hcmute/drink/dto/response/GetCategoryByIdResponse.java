package com.hcmute.drink.dto.response;

import com.hcmute.drink.enums.CategoryStatus;
import lombok.Data;

@Data
public class GetCategoryByIdResponse {
    private String id;
    private String code;
    private String name;
    private String imageUrl;
    private CategoryStatus status;
}
