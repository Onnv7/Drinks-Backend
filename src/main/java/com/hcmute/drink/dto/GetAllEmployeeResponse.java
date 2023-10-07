package com.hcmute.drink.dto;

import lombok.Data;

@Data
public class GetAllEmployeeResponse {
    private String id;
    private String firstName;
    private String lastName;

    private boolean enabled = true;

}
