package com.hcmute.drink.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetReleaseCouponByIdResponse {
    private List<String> conditionList;
    private String description;
    private String code;
    private Date startDate;
    private Date expirationDate;
}
