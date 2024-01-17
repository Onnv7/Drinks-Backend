package com.hcmute.drink.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class GetReleaseCouponListResponse {
    private String id;
    private String description;
    private Date startDate;
    private Date expirationDate;
}
