package com.hcmute.drink.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmute.drink.enums.Gender;
import com.hcmute.drink.enums.Role;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.List;

@Data
public class GetAllUserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;
    private String email;
//    private String phoneNumber;

    private boolean enabled;
    private Date updatedAt;
}
