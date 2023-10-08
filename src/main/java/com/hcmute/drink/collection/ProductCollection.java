package com.hcmute.drink.collection;


import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.collection.embedded.SizeEmbedded;
import com.hcmute.drink.collection.embedded.ToppingEmbedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "product")
public class ProductCollection{
    @Id
    private String id;
    private String name;
    private List<ImageEmbedded> imageList;
//    private double price;
    private List<SizeEmbedded> sizeList;
    private String description;
    @UniqueElements
    private List<ToppingEmbedded> toppingList;

    private ObjectId categoryId;

    private boolean deleted = false;
    
    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
