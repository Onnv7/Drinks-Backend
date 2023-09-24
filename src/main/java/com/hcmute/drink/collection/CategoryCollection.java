package com.hcmute.drink.collection;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "category")
public class CategoryCollection {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String imageUrl;
    private String publicId;
}
