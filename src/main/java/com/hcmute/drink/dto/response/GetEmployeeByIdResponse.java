package com.hcmute.drink.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmute.drink.enums.Gender;
import com.hcmute.drink.enums.Role;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
public class GetEmployeeByIdResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;
    private boolean enabled;
}
