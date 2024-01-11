package com.hcmute.drink.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String code;
    private String province;
    private String commune;
    private String district;
    private String details;
}
