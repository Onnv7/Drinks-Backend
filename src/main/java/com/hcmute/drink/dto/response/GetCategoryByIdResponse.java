package com.hcmute.drink.dto.response;

import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.enums.CategoryStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class GetCategoryByIdResponse {
    private String id;
    private String code;
    private String name;
    private ImageEmbedded image;
    private CategoryStatus status;
}
