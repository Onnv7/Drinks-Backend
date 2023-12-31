package com.hcmute.drink;

//import com.hcmute.drink.config.TwilioConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
public class DrinkApplication {

    public static void main(String[] args) {

//        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(DrinkApplication.class, args);
    }
}