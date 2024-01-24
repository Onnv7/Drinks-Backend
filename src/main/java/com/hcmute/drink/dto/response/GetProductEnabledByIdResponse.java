package com.hcmute.drink.dto.response;

import com.hcmute.drink.collection.embedded.SizeEmbedded;
import com.hcmute.drink.collection.embedded.ToppingEmbedded;
import com.hcmute.drink.enums.ProductStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class GetProductEnabledByIdResponse {
    @Id
    private String id;
    private String name;
    private String imageUrl;
    private List<SizeEmbedded> sizeList;
    private String description;

    private List<ToppingEmbedded> toppingList;
    private ProductStatus status;
}
