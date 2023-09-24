package com.hcmute.drink.collection;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hcmute.drink.common.Gender;
import com.hcmute.drink.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCollection {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;
    @JsonIgnore
    private String password;
    @Indexed(unique = true)
    private String email;
    private String phoneNumber;
    private String address;
    private String introduction;
    @Builder.Default
    private Role[] roles = {Role.ROLE_USER};
    private int star;

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private boolean verifiedEmail = false;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

}