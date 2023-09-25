package com.hcmute.drink.dto;

import com.hcmute.drink.common.ImageModel;
import com.hcmute.drink.common.ToppingModel;
import lombok.Data;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateProductRequest {

    private String name;
    private List<MultipartFile> images;
    private double price;
    private List<String> size;
    private String description;
    private List<ToppingModel> toppingList;
    private ObjectId categoryId;
}
