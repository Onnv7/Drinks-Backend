package com.hcmute.drink.dto;

import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.SizeModel;
import com.hcmute.drink.common.ToppingModel;
import lombok.Data;

import java.util.List;

@Data
public class GetProductByIdResponse {
    private String id;
    private String name;
    private List<ImageModel> imageList;
    private List<SizeModel> sizeList;
    private String description;
    private List<ToppingModel> toppingList;
    private String categoryId;
}
