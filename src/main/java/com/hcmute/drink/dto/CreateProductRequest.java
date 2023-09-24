package com.hcmute.drink.dto;

import com.hcmute.drink.common.ToppingModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateProductRequest {
    @NotBlank
    private String name;
    @NotBlank
    private double price;
    private List<String> size;
    @NotBlank
    private String description;
    private List<ToppingModel> toppingList;
}
