package com.hcmute.drink.dto;

import com.hcmute.drink.enums.Gender;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetUserByIdResponse {
    private String id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;
    private String email;
    private String phoneNumber;
}
