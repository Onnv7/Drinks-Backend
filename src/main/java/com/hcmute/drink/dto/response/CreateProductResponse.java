package com.hcmute.drink.dto.response;

import com.hcmute.drink.collection.embedded.SizeEmbedded;
import com.hcmute.drink.dto.common.ImageDto;
import com.hcmute.drink.dto.common.ToppingDto;
import lombok.Data;

import java.util.List;

@Data
public class CreateProductResponse {
    private String id;
    private String name;
    private List<ImageDto> imageList;
    private List<SizeEmbedded> sizeList;
    private String description;
    private List<ToppingDto> toppingList;
    private String categoryId;

}
