package com.hcmute.drink.collection;


import com.hcmute.drink.common.ImageModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

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
    private ImageModel image;


    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
