package com.hcmute.drink.dto;

import lombok.Data;

@Data
public class GetRevenueByTimeResponse {
    private Time time;
    private double revenue;

    @Data
    private class Time {
        int year;
        int month;
        int day;
    }
}
