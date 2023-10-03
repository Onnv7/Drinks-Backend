package com.hcmute.drink.collection;

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
@Document(collection = "confirmation")
public class ConfirmationCollection {
    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    @Indexed(unique = true)
    private String email;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
