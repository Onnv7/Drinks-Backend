package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hcmute.drink.dto.common.SizeDto;
import com.hcmute.drink.dto.common.ToppingDto;
import com.hcmute.drink.enums.ProductStatus;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetProductByIdResponse {
    private String id;
    private String code;
    private String name;
    private List<SizeDto> sizeList;
    private String description;
    private List<ToppingDto> toppingList;
    private String categoryId;
    private ProductStatus status;
    private String imageUrl;
}
