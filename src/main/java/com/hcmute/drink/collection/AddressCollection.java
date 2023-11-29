package com.hcmute.drink.collection;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

import static com.hcmute.drink.constant.SwaggerConstant.BOOLEAN_EX;
import static com.hcmute.drink.constant.SwaggerConstant.NOT_NULL_DES;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "address")
public class  AddressCollection {
    @Id
    private String id;
    private String details;
    private double longitude;
    private double latitude;
    private String note;
    private String recipientName;
    private String phoneNumber;
    private ObjectId userId;

    private boolean isDefault = false;

//    @CreatedDate
//    private Date createdAt;
//
//    @LastModifiedDate
//    private Date updatedAt;
}
