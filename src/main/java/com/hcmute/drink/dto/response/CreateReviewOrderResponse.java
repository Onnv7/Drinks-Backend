package com.hcmute.drink.dto.response;

import lombok.Data;

@Data
public class CreateReviewOrderResponse {
    private int rating;
    private String content;
}
