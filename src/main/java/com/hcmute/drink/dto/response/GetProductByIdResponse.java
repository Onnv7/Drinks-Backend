package com.hcmute.drink.dto.response;

import com.hcmute.drink.common.ImageDto;
import com.hcmute.drink.common.SizeDto;
import com.hcmute.drink.common.ToppingDto;
import com.hcmute.drink.enums.ProductStatus;
import lombok.Data;

import java.util.List;

@Data
public class GetProductByIdResponse {
    private String id;
    private String code;
    private String name;
    private ImageDto image;
    private List<SizeDto> sizeList;
    private String description;
    private List<ToppingDto> toppingList;
    private String categoryId;
    private ProductStatus status;
}
