package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.ConfirmationCollection;
import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.common.Role;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.LoginResponse;
import com.hcmute.drink.repository.ConfirmationRepository;
import com.hcmute.drink.repository.UserRepository;
import com.hcmute.drink.dto.RegisterRequest;
import com.hcmute.drink.security.UserPrincipal;
import com.hcmute.drink.service.AuthService;
import com.hcmute.drink.utils.JwtUtils;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;
    private final ConfirmationRepository confirmationRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtIssuer;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager;
    @Value("${twilio.trial_number}")
    private String trialNumber;

    @Override
    public LoginResponse attemptLogin(String email, String password) throws Exception {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        var principal = (UserPrincipal) authentication.getPrincipal();
        UserCollection user = userRepository.findByEmail(principal.getEmail());
        if (!user.isVerifiedEmail()) {
            throw new Exception(ErrorConstant.EMAIL_UNVERIFIED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var roles = principal.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();

        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        return LoginResponse.builder().accessToken(token).build();
    }

    @Override
    public void registerUser(RegisterRequest body) throws Exception {
        Date now = new Date();
        UserCollection existedUser = userRepository.findByEmail(body.getEmail());

        if (existedUser != null) {
            throw new Exception(ErrorConstant.REGISTERED_EMAIL);
        }
        UserCollection newUser = UserCollection.builder()
                .id(UUID.randomUUID().toString())
                .roles((new Role[]{Role.ROLE_USER}))
                .createdAt(now)
                .updatedAt(now)
                .build();
        modelMapper.map(body, newUser);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if (userRepository.save(newUser) != null) {
            ConfirmationCollection confirmation = ConfirmationCollection.builder()
                    .email(newUser.getEmail())
                    .token(UUID.randomUUID().toString())
                    .build();
            confirmationRepository.save(confirmation);
            emailService.sendHtmlVerifyEmail(newUser.getFirstName(), newUser.getEmail(), confirmation.getToken());
            return;
        }
        throw new Exception(ErrorConstant.CREATED_FAILED);
    }

    @Override
    public void resendTokenToEmail(String email) throws Exception {
        UserCollection user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception(ErrorConstant.USER_NOT_FOUND);
        }
        ConfirmationCollection confirmation = confirmationRepository.findByEmail(user.getEmail());
        String token = UUID.randomUUID().toString();
        confirmation.setToken(token);
        confirmationRepository.save(confirmation);
        emailService.sendHtmlVerifyEmail(user.getFirstName(), user.getEmail(), token);

    }

    @Override
    public void sendCodeToEmail(String email, String code) throws Exception {
        UserCollection user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception(ErrorConstant.USER_NOT_FOUND);
        }
        emailService.sendHtmlVerifyCode(user.getFirstName(), user.getEmail(), code);

    }

    @Override
    public void sendMessageToPhoneNumber(String phoneNumber, String text) throws Exception {
        PhoneNumber to = new PhoneNumber(phoneNumber);
        PhoneNumber from = new PhoneNumber(trialNumber);
        Message message = Message.creator(to, from, text).create();
    }

    @Override
    public boolean verifyEmail(String token) {
        ConfirmationCollection confirmationCollection = confirmationRepository.findByToken(token);
        if (confirmationCollection != null && token.equals(confirmationCollection.getToken())) {
            confirmationRepository.deleteById(confirmationCollection.getId());
            UserCollection user = userRepository.findByEmail(confirmationCollection.getEmail());
            user.setVerifiedEmail(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
