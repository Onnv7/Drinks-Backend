package com.hcmute.drink.dto;

import com.hcmute.drink.collection.embedded.SizeEmbedded;
import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.ToppingModel;
import lombok.Data;

import java.util.List;

@Data
public class CreateProductResponse {
    private String id;
    private String name;
    private List<ImageModel> imageList;
    private List<SizeEmbedded> sizeList;
    private String description;
    private List<ToppingModel> toppingList;
    private String categoryId;

}
