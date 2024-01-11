package com.hcmute.drink.schedule;

import com.hcmute.drink.enums.OrderStatus;
import com.hcmute.drink.service.database.implement.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderSchedule {
    private final OrderService orderService;

    @Scheduled(cron = "1 0 0 * * *")
    public void cancelOrderPreviousDay() {
        orderService.cancelOrderPreviousDay(OrderStatus.CREATED);
        orderService.cancelOrderPreviousDay(OrderStatus.ACCEPTED);
    }
}
