package com.hcmute.drink.dto;

import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.collection.embedded.ToppingEmbedded;
import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.ToppingModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class GetAllProductsResponse  {
    private String id;
    private String name;
    public List<ImageModel> imagesList;
    private double price;
    private List<String> size;
    private String description;
    private List<ToppingModel> toppingList;
    private String categoryId;
}
