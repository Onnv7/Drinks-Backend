package com.hcmute.drink.collection;


import com.hcmute.drink.collection.embedded.ImageEmbedded;
import com.hcmute.drink.enums.CategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "category")
public class CategoryCollection {

    @Transient
    public static final String SEQUENCE_NAME = "category_sequence";

    @Transient
    public static final String PREFIX_CODE = "C";
    @Transient
    public static final int LENGTH_NUMBER = 3;

    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    @Indexed(unique = true)
    private String name;
    private ImageEmbedded image;

    @Builder.Default
    private CategoryStatus status = CategoryStatus.HIDDEN;

    // TODO: check chỗ update product thay category khác thì có thể set lại canDelete = true
    @Builder.Default
    private boolean canDelete = true;

    @Builder.Default
    private boolean isDeleted = false;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
