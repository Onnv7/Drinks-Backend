package com.hcmute.drink.dto;

import lombok.Data;

@Data
public class GetOrderQuantityByStatusResponse {
    private int orderQuantity;
    private int difference;
}
