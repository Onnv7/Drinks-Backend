package com.hcmute.drink.dto.response;

import com.hcmute.drink.enums.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateEmployeeForAdminResponse {
    private String id;
    private String username;

    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;
}
