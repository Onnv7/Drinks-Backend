package com.hcmute.drink.dto;

import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.SizeModel;
import com.hcmute.drink.common.ToppingModel;
import lombok.Data;

import java.util.List;

@Data
public class UpdateProductResponse {
    private String id;
    private String name;
    private ImageModel image;
    private List<SizeModel> sizeList;
    private String description;
    private List<ToppingModel> toppingList;
    private String categoryId;
    private boolean enabled;

}
