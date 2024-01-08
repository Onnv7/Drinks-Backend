package com.hcmute.drink.dto.response;

import com.hcmute.drink.common.ImageModel;
import lombok.Data;

@Data
public class UpdateCategoryResponse {
    private String id;
    private String name;
    private ImageModel image;
    private boolean enabled;
}
