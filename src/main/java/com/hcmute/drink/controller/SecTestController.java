package com.hcmute.drink.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sec")
public class SecTestController {
    @GetMapping()
    private String greeting() {
        return "Hello";
    }
}
