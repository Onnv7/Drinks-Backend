package com.hcmute.drink.collection;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCollection {
    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";

    @Transient
    public static final String PREFIX_CODE = "U";

    @Transient
    public static final int LENGTH_NUMBER = 9;

    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;
    @JsonIgnore
    private String password;
    @Indexed(unique = true)
    private String email;
    private String phoneNumber;

    @Builder.Default
    private Role[] roles = {Role.ROLE_USER};

    @Builder.Default
    private boolean enabled = true;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }

}