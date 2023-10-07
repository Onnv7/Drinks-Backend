package com.hcmute.drink.dto;

import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.common.ImageModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class GetCategoryResponse {
    private String id;
    private String name;
    private ImageModel image;
}
