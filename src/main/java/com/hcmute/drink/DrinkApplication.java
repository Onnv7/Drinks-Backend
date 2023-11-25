package com.hcmute.drink;

//import com.hcmute.drink.config.TwilioConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class DrinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrinkApplication.class, args);
    }
}