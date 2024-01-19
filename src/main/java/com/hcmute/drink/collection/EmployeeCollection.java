package com.hcmute.drink.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmute.drink.enums.EmployeeStatus;
import com.hcmute.drink.enums.Gender;
import com.hcmute.drink.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
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
@Document(collection = "employee")
public class EmployeeCollection {

    @Transient
    public static final String SEQUENCE_NAME = "employee_sequence";

    @Transient
    public static final String PREFIX_CODE = "E";
    @Transient
    public static final int LENGTH_NUMBER = 4;

    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    @Indexed(unique = true)
    private String username;
    @JsonIgnore
    private String password;

    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;
    private String phoneNumber;

    @Builder.Default
    private Role[] roles = {Role.ROLE_EMPLOYEE};

    private ObjectId branchId;

    @Builder.Default
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @Builder.Default
    private boolean isDeleted = false;
    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
