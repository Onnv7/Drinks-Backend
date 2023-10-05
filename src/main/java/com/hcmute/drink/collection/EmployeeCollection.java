package com.hcmute.drink.collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmute.drink.enums.Gender;
import com.hcmute.drink.enums.Role;
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
@Document(collection = "employee")
public class EmployeeCollection {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @JsonIgnore
    private String password;

    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;

    @Builder.Default
    private Role[] roles = {Role.ROLE_EMPLOYEE};


    @Builder.Default
    private boolean enabled = true;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
