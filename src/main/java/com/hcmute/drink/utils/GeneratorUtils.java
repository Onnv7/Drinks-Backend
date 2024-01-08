package com.hcmute.drink.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCodeUtils {
    public String generateRandomCode(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomNumber = random.nextInt(10);
            code.append(randomNumber);
        }

        return code.toString();
    }
}
