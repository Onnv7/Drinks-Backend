package com.hcmute.drink.dto.response;

import com.hcmute.drink.common.ImageDto;
import lombok.Data;

@Data
public class GetAllCategoryResponse {
    private String id;
    private String code;
    private String name;
    private ImageDto image;
    private boolean enabled;
}
