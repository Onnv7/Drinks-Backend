package com.hcmute.drink.dto;

import com.hcmute.drink.collection.embedded.ImageEmbedded;
import lombok.Data;

@Data
public class GetAllCategoriesWithoutDisabledResponse {
    private String id;
    private String name;
    private ImageEmbedded image;
}
