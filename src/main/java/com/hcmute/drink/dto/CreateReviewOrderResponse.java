package com.hcmute.drink.dto;

import lombok.Data;

@Data
public class CreateReviewOrderResponse {
    private int rating;
    private String content;
}
