package com.hcmute.drink.collection;


import com.hcmute.drink.common.ToppingModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "product")
public class ProductCollection {
    @Id
    private String id;
    private String name;
    private List<String> imageUrls;
    private double price;
    private List<String> size;
    private String description;
    private List<ToppingModel> toppingList;
    private String categoryId;
}
