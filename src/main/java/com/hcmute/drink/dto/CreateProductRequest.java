package com.hcmute.drink.dto;

import com.hcmute.drink.collection.CategoryCollection;
import com.hcmute.drink.common.ToppingModel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateProductRequest {
    @NotBlank
    private String name;
    @Min(1000)
    private double price;
    private List<MultipartFile> images;
    private List<String> size;
    @NotBlank
    private String description;
    private List<ToppingModel> toppingList;
    @NotNull
    private ObjectId categoryId;
}
