package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.ConfirmationCollection;
import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.dto.RegisterResponse;
import com.hcmute.drink.enums.Role;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.LoginResponse;
import com.hcmute.drink.repository.ConfirmationRepository;
import com.hcmute.drink.repository.TokenRepository;
import com.hcmute.drink.repository.UserRepository;
import com.hcmute.drink.security.UserPrincipal;
import com.hcmute.drink.security.custom.user.UserUsernamePasswordAuthenticationToken;
import com.hcmute.drink.service.AuthService;
import com.hcmute.drink.utils.EmailUtils;
import com.hcmute.drink.utils.JwtUtils;
import com.hcmute.drink.utils.RandomCodeUtils;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RandomCodeUtils randomCodeUtils;
    private final EmailUtils emailService;
    private final ConfirmationRepository confirmationRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AuthenticationManager authenticationManager;
    private final ConfirmationServiceImpl confirmationService;
    private final UserServiceImpl userService;
    private final TokenServiceImpl tokenService;

    @Value("${twilio.trial_number}")
    private String trialNumber;

    @Override
    public LoginResponse attemptLogin(String email, String password) throws Exception {
        UserPrincipal principal = UserPrincipal.builder()
                .username(email)
                .password(password)
                .build();

        Authentication userCredential = new UserUsernamePasswordAuthenticationToken(principal);
        var authentication = authenticationManager.authenticate(
                userCredential
        );

        var principalAuthenticated = (UserPrincipal) authentication.getPrincipal();
        UserCollection user = userRepository.findByEmail(principalAuthenticated.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var roles = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();

        var token = jwtUtils.issueAccessToken(principalAuthenticated.getUserId(), principalAuthenticated.getUsername(), roles);
        String refreshToken = jwtUtils.issueRefreshToken(principalAuthenticated.getUserId(), principalAuthenticated.getUsername(), roles);
        tokenService.createToken(refreshToken, principalAuthenticated.getUserId());

        return LoginResponse.builder().accessToken(token).userId(principalAuthenticated.getUserId()).refreshToken(refreshToken).build();
    }

    @Override
    public RegisterResponse registerUser(UserCollection data) throws Exception {
        UserCollection existedUser = userRepository.findByEmail(data.getEmail());

        if (existedUser != null) {
            throw new Exception(ErrorConstant.REGISTERED_EMAIL);
        }
        RegisterResponse resData = new RegisterResponse();
        modelMapper.map(data, resData);

        data.setRoles(new Role[]{Role.ROLE_USER});
        data.setPassword(passwordEncoder.encode(data.getPassword()));
        UserCollection savedUser = userRepository.save(data);
        if (savedUser == null) {
            throw new Exception(ErrorConstant.CREATED_FAILED);
        }
        List<String> roleList = new ArrayList<>();
        for (Role role : savedUser.getRoles()) {
            roleList.add(role.name());
        }
        var token = jwtUtils.issueAccessToken(savedUser.getId(), savedUser.getEmail(), roleList);
        resData.setAccessToken(token);
        resData.setUserId(savedUser.getId());
        return resData;
    }

    public void resendCode(String email) throws Exception {
        ConfirmationCollection confirmation = confirmationRepository.findByEmail(email);
        if(confirmation == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + email);
        }

        String code = randomCodeUtils.generateRandomCode(6);
        confirmation.setCode(code);
        confirmationService.updateConfirmationInfo(confirmation);
        emailService.sendHtmlVerifyCodeToRegister(email, code);

    }

    @Override
    public void sendCodeToRegister(String email) throws Exception {
        UserCollection user = userRepository.findByEmail(email);
        if (user != null) {
            throw new Exception(ErrorConstant.REGISTERED_EMAIL);
        }
        String code = randomCodeUtils.generateRandomCode(6);
        confirmationService.createOrUpdateConfirmationInfo(email, code);
        emailService.sendHtmlVerifyCodeToRegister(email, code);
    }

    public void sendCodeToGetPassword(String email) throws Exception {
        userService.exceptionIfNotExistedUserByEmail(email);
        String code = randomCodeUtils.generateRandomCode(6);
        confirmationService.createOrUpdateConfirmationInfo(email, code);
        emailService.sendHtmlVerifyCodeToRegister(email, code);
    }

    @Override
    public void sendMessageToPhoneNumber(String phoneNumber, String text) {
        PhoneNumber to = new PhoneNumber(phoneNumber);
        PhoneNumber from = new PhoneNumber(trialNumber);
        Message message = Message.creator(to, from, text).create();
    }

    public boolean verifyCodeByEmail(String code, String email) throws Exception {
        ConfirmationCollection confirmationCollection = confirmationRepository.findByEmail(email);
        if (confirmationCollection != null && code.equals(confirmationCollection.getCode())) {
            confirmationRepository.deleteById(confirmationCollection.getId());
            return true;
        }
        if (confirmationCollection == null) {
            throw new Exception(ErrorConstant.NOT_FOUND);
        }

        throw new Exception(ErrorConstant.EMAIL_UNVERIFIED);
    }

}
