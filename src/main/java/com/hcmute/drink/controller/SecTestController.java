package com.hcmute.drink.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/api/sec")
public class SecTestController {
    @GetMapping()
    private String greeting() {
        return "Hello";
    }
}
