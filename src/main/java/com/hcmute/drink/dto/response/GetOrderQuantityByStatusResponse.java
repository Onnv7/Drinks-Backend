package com.hcmute.drink.dto.response;

import lombok.Data;

@Data
public class GetOrderQuantityByStatusResponse {
    private int orderQuantity;
    private int difference;
}
