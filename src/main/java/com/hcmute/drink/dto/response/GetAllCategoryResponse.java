package com.hcmute.drink.dto.response;

import com.hcmute.drink.dto.common.ImageDto;
import com.hcmute.drink.enums.CategoryStatus;
import lombok.Data;

@Data
public class GetAllCategoryResponse {
    private String id;
    private String code;
    private String name;
    private ImageDto image;
    private CategoryStatus status;
}
