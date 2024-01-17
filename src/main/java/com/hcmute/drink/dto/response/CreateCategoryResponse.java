package com.hcmute.drink.dto.response;

import com.hcmute.drink.dto.common.ImageDto;
import lombok.Data;

@Data
public class CreateCategoryResponse {
    private String id;
    private String name;
    private ImageDto image;
    private boolean enabled;
}
