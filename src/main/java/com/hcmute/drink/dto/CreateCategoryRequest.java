package com.hcmute.drink.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateCategoryRequest {
    private MultipartFile image;
    private String name;
}
