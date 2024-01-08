package com.hcmute.drink.dto.response;

import com.hcmute.drink.enums.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class GetAllEmployeeResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private Date birthDate;
    private Gender gender;
    private boolean enabled;

}
