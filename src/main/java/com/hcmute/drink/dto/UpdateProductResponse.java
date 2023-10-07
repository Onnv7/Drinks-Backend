package com.hcmute.drink.dto;

import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.ToppingModel;
import lombok.Data;

import java.util.List;

@Data
public class UpdateProductResponse {
    private String id;
    private String name;
    private List<ImageModel> imagesList;
    private double price;
    private List<String> size;
    private String description;
    private List<ToppingModel> toppingList;
    private String categoryId;

}
