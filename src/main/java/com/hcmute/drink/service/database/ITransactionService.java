package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.response.GetRevenueByTimeResponse;
import com.hcmute.drink.dto.response.GetRevenueCurrentDateResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ITransactionService {
    void updateTransaction(String id, HttpServletRequest request);
    void completeTransaction(String transId);
    List<GetRevenueByTimeResponse> getRevenueByTime(String time);
    GetRevenueCurrentDateResponse getRevenueCurrentDate();
}
