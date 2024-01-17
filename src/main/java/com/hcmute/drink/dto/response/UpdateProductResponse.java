package com.hcmute.drink.dto.response;

import com.hcmute.drink.dto.common.ImageDto;
import com.hcmute.drink.dto.common.SizeDto;
import com.hcmute.drink.dto.common.ToppingDto;
import lombok.Data;

import java.util.List;

@Data
public class UpdateProductResponse {
    private String id;
    private String name;
    private ImageDto image;
    private List<SizeDto> sizeList;
    private String description;
    private List<ToppingDto> toppingList;
    private String categoryId;
    private boolean enabled;

}
