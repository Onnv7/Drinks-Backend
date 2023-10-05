package com.hcmute.drink.payment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final VNPayUtils vnPayUtils;

    @GetMapping("/create_payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request, @RequestParam("vnp_Amount") long amount,
                                           @RequestParam("vnp_OrderInfo") String orderInfo
    ) {
        try {
            String url = vnPayUtils.createPayment(request, amount*100, orderInfo);
            PaymentResDTO paymentResDTO = new PaymentResDTO();
            paymentResDTO.setStatus("0k");
            paymentResDTO.setMessage("success");
            paymentResDTO.setURL(url);
            return ResponseEntity.status(HttpStatus.OK).body(paymentResDTO);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refund(HttpServletRequest request) {
        try {

//            vnPayUtils.refund(request);
            return new ResponseEntity<>("res", HttpStatus.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/IPN")
    public ResponseEntity<?> payResult(HttpServletRequest request) {
        try {

            IPNResDto res = vnPayUtils.getResult(request);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
