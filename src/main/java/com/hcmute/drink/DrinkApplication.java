package com.hcmute.drink;

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
//@EnableMongoRepositories(basePackages = "com.hcmute.drink.repository.database")
@EnableElasticsearchRepositories(basePackages = "com.hcmute.drink.repository.elasticsearch")
//@ComponentScan(basePackages = "com.hcmute.drink.repository.")
public class DrinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrinkApplication.class, args);
    }
}