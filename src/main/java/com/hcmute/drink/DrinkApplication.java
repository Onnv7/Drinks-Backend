package com.hcmute.drink;

//import com.hcmute.drink.config.TwilioConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
@EnableTransactionManagement
public class DrinkApplication {

    public static void main(String[] args) {

        SpringApplication.run(DrinkApplication.class, args);
    }
}