package com.hcmute.drink.dto;

import com.hcmute.drink.common.ImageModel;
import lombok.Data;

@Data
public class UpdateCategoryResponse {
    private String id;
    private String name;
    private ImageModel image;
}
