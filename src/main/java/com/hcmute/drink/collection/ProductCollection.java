package com.hcmute.drink.collection;


import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.collection.embedded.SizeEmbedded;
import com.hcmute.drink.collection.embedded.ToppingEmbedded;
import com.hcmute.drink.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "product")
public class ProductCollection{
    @Transient
    public static final String SEQUENCE_NAME = "product_sequence";
    @Transient
    public static final String PREFIX_CODE = "PD";
    @Transient
    public static final int LENGTH_NUMBER = 3;

    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    private String name;
//    private List<ImageEmbedded> imageList;
    private ImageEmbedded thumbnail;
    private ImageEmbedded image;
//    private double price;
    private List<SizeEmbedded> sizeList;
    private String description;
    @UniqueElements
    private List<ToppingEmbedded> toppingList;

    private ObjectId categoryId;

//    private boolean enabled = true;
    private ProductStatus status;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
