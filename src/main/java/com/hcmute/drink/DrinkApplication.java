package com.hcmute.drink;

//import com.hcmute.drink.config.TwilioConfig;
import com.hcmute.drink.utils.TwilioUtils;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class DrinkApplication {

    @Autowired
    private TwilioUtils twilioUtils;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(twilioUtils.getAccountSid(), twilioUtils.getAuthToken());
    }
    public static void main(String[] args) {
        SpringApplication.run(DrinkApplication.class, args);
    }
}