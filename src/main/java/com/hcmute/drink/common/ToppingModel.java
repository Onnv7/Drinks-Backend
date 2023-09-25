package com.hcmute.drink.common;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public class ToppingModel {
    @NotBlank
    private String name;
    @Min(5000)
    private double price;
}
