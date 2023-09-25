package com.hcmute.drink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateCategoryRequest {
    @NotNull
    private MultipartFile image;
    @NotBlank
    private String name;
}
