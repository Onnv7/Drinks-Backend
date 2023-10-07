package com.hcmute.drink.dto;

import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.collection.embedded.ToppingEmbedded;
import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.ToppingModel;
import lombok.Data;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class CreateProductResponse {
    private String id;
    private String name;
    private List<ImageModel> imagesList;
    private double price;
    private List<String> size;
    private String description;
    private List<ToppingModel> toppingList;
    private String categoryId;

}
