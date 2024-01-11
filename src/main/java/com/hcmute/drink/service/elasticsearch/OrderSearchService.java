package com.hcmute.drink.service.elasticsearch;

import com.hcmute.drink.collection.OrderCollection;
import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.model.elasticsearch.OrderIndex;
import com.hcmute.drink.repository.elasticsearch.OrderSearchRepository;
import com.hcmute.drink.service.database.implement.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSearchService {
    private final UserService userService;
    private final OrderSearchRepository orderSearchRepository;
    public OrderIndex createOrder(OrderCollection orderCollection) {
        UserCollection user = userService.getById(orderCollection.getUserId().toString());
        OrderIndex orderIndex = OrderIndex.builder()
                .id(orderCollection.getId())
                .code(orderCollection.getCode())
                .customerName(user.getFirstName() + " " + user.getLastName())
                .recipientName(orderCollection.getAddress().getRecipientName())
                .phoneNumber(orderCollection.getAddress().getPhoneNumber())
//                .productQuantity(orderCollection.getProducts().size())
//                .thumbnailUrl("")
//                .orderType(orderCollection.getOrderType())
                .customerCode(user.getCode())
                .build();
        return orderSearchRepository.save(orderIndex);
    }
}
