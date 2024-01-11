package com.hcmute.drink.dto.response;

import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.SizeModel;
import com.hcmute.drink.common.ToppingModel;
import com.hcmute.drink.enums.ProductStatus;
import lombok.Data;

import java.util.List;

@Data
public class GetProductByIdResponse {
    private String id;
    private String code;
    private String name;
    private ImageModel image;
    private List<SizeModel> sizeList;
    private String description;
    private List<ToppingModel> toppingList;
    private String categoryId;
    private ProductStatus status;
}
