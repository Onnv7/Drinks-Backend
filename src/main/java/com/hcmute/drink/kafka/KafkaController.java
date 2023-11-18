package com.hcmute.drink.kafka;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.GetAllShippingOrdersResponse;
import com.hcmute.drink.model.ResponseAPI;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.ORDER_GET_ALL_SHIPPING_SUB_PATH;

@RestController
@RequestMapping("/kafka")
@Tag(name = "Kafka")
public class KafkaController {

    @Autowired
    private KafkaMessagePublisher publisher;

    @GetMapping(path = "/publish/{message}")
    public ResponseEntity<?> publishMessage(@PathVariable String message) {
        try {
            for (int i = 0; i < 1; i++) {

//                publisher.sendMessageToTopic(message + " " + i);
            }
            return ResponseEntity.ok("OKOKOK");
        }
         catch (Exception e) {
            return ResponseEntity.badRequest().body("Not ok");
         }
    }

}
