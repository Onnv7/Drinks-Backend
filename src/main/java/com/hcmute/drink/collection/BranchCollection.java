package com.hcmute.drink.collection;

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
@Document(collection = "branch")
public class BranchCollection {
    @Transient
    public static final String SEQUENCE_NAME = "branch_sequence";
    @Transient
    public static final String PREFIX_CODE = "SF";
    @Transient
    public static final int LENGTH_NUMBER = 2;
    @Id
    private String id;

    @Indexed(unique = true)
    private String code;
    private String province;
    private String district;
    private String ward;
    private String details;
    private double longitude;
    private double latitude;
    private String phoneNumber;
    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
    public String getFullAddress() {
        return this.getDetails() + " " + this.getWard() + " " + this.getDistrict() + " " + this.getProvince();
    }
}
