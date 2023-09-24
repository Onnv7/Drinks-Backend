package com.hcmute.drink.utils;

//import com.hcmute.drink.config.TwilioConfig;
import com.hcmute.drink.dto.VerifyPhoneNumberRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
@Data
public class TwilioUtils {
//    @Autowired
//    private TwilioConfig twilioConfig;


    @Value("${twilio.account_sid}")
    private String accountSid;
    @Value("${twilio.auth_token}")
    private String authToken;
    @Value("${twilio.trial_number}")
    private String trialNumber;

    public void sendOTP(VerifyPhoneNumberRequest body) {
        PhoneNumber to = new PhoneNumber(body.getPhoneNumber());
        PhoneNumber from = new PhoneNumber(trialNumber);
        Message message = Message.creator(
                        to,
                        from,
                        body.getMessage())
                .create();
    }
}
