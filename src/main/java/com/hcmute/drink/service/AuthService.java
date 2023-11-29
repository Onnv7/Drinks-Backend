package com.hcmute.drink.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hcmute.drink.collection.ConfirmationCollection;
import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.collection.TokenCollection;
import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.dto.*;
import com.hcmute.drink.enums.Role;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.kafka.KafkaMessagePublisher;
import com.hcmute.drink.model.redis.EmployeeToken;
import com.hcmute.drink.model.redis.UserToken;
import com.hcmute.drink.repository.ConfirmationRepository;
import com.hcmute.drink.repository.EmployeeRepository;
import com.hcmute.drink.repository.UserRepository;
import com.hcmute.drink.security.UserPrincipal;
import com.hcmute.drink.security.custom.employee.EmployeeUsernamePasswordAuthenticationToken;
import com.hcmute.drink.security.custom.user.UserUsernamePasswordAuthenticationToken;
import com.hcmute.drink.service.redis.EmployeeRefreshTokenRedisService;
import com.hcmute.drink.service.redis.UserRefreshTokenRedisService;
import com.hcmute.drink.utils.EmailUtils;
import com.hcmute.drink.utils.JwtUtils;
import com.hcmute.drink.utils.RandomCodeUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hcmute.drink.constant.ErrorConstant.*;
import static com.hcmute.drink.utils.JwtUtils.ROLES_CLAIM_KEY;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final UserRepository userRepository;
    private final RandomCodeUtils randomCodeUtils;
    private final EmailUtils emailUtils;
    private final ConfirmationRepository confirmationRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    @Autowired
    @Lazy
    private  PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationService confirmationService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final UserRefreshTokenRedisService userRefreshTokenRedisService;
    private final EmployeeRefreshTokenRedisService employeeRefreshTokenRedisService;

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
        String userId = principalAuthenticated.getUserId();
        String username = principalAuthenticated.getUsername();
        var accessToken = jwtUtils.issueAccessToken(userId, username, roles);
        String refreshToken = jwtUtils.issueRefreshToken(userId, username, roles);

        userRefreshTokenRedisService.createNewUserRefreshToken(refreshToken, principalAuthenticated.getUserId());
        return LoginResponse.builder().accessToken(accessToken).userId(userId).refreshToken(refreshToken).build();
    }
    public EmployeeLoginResponse attemptEmployeeLogin(String username, String password) throws Exception {
        UserPrincipal principal = UserPrincipal.builder()
                .username(username)
                .password(password)
                .build();
        Authentication employeeCredential = new EmployeeUsernamePasswordAuthenticationToken(principal);
        var authentication = authenticationManager.authenticate(employeeCredential);

        var principalAuthenticated = (UserPrincipal) authentication.getPrincipal();
        EmployeeCollection employee = employeeRepository.findByUsername(principalAuthenticated.getUsername());

        if(!employee.isEnabled()) {
            throw new Exception(ErrorConstant.ACCOUNT_BLOCKED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var roles = principalAuthenticated.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();

        var accessToken = jwtUtils.issueAccessToken(principalAuthenticated.getUserId(), principalAuthenticated.getUsername(), roles);
        String refreshToken = jwtUtils.issueRefreshToken(principalAuthenticated.getUserId(), principalAuthenticated.getUsername(), roles);

        employeeRefreshTokenRedisService.createNewEmployeeRefreshToken(refreshToken, principalAuthenticated.getUserId());
        return EmployeeLoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).employeeId(principalAuthenticated.getUserId()).build();
    }

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
        var accessToken = jwtUtils.issueAccessToken(savedUser.getId(), savedUser.getEmail(), roleList);
        var refreshToken = jwtUtils.issueRefreshToken(savedUser.getId(), savedUser.getEmail(), roleList);
        resData.setAccessToken(accessToken);
        resData.setRefreshToken(refreshToken);
        resData.setUserId(savedUser.getId());
        return resData;
    }

    public void resendCode(String email) throws Exception {
        ConfirmationCollection confirmation = confirmationRepository.findByEmail(email);
        if (confirmation == null) {
            throw new Exception(ErrorConstant.NOT_FOUND + email);
        }

        String code = randomCodeUtils.generateRandomCode(6);
        confirmation.setCode(code);
        confirmationService.updateConfirmationInfo(confirmation);
        kafkaMessagePublisher.sendMessageToCodeEmail(new CodeEmailDto(code, email));

    }


    public void sendCodeToRegister(String email) throws Exception {
        UserCollection user = userRepository.findByEmail(email);
        if (user != null) {
            throw new Exception(ErrorConstant.REGISTERED_EMAIL);
        }
        String code = randomCodeUtils.generateRandomCode(6);
        confirmationService.createOrUpdateConfirmationInfo(email, code);
        kafkaMessagePublisher.sendMessageToCodeEmail(new CodeEmailDto(code, email));
    }

    public void sendCodeToGetPassword(String email) throws Exception {
        userService.exceptionIfNotExistedUserByEmail(email);
        String code = randomCodeUtils.generateRandomCode(6);
        confirmationService.createOrUpdateConfirmationInfo(email, code);
        kafkaMessagePublisher.sendMessageToCodeEmail(new CodeEmailDto(code, email));
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

    public RefreshTokenResponse refreshToken(String refreshToken) throws Exception {
        DecodedJWT jwt = jwtUtils.decodeRefreshToken(refreshToken);


        String userId = jwt.getSubject().toString();
        UserToken token =  userRefreshTokenRedisService.getInfoOfRefreshToken(refreshToken, userId);

        UserCollection user = userService.exceptionIfNotExistedUserById(userId);

        if (token == null) {
            throw new Exception(INVALID_TOKEN);
        }
        if(token.isUsed()) {
            userRefreshTokenRedisService.deleteUserRefreshToken(userId);
            throw new Exception(STOLEN_TOKEN);
        }
        List<String> roles = jwt.getClaim(ROLES_CLAIM_KEY).asList(String.class);
        String newAccessToken = jwtUtils.issueAccessToken(user.getId(), user.getEmail(), roles);
        String newRefreshToken = jwtUtils.issueRefreshToken(user.getId(), user.getEmail(), roles);
        userRefreshTokenRedisService.createNewUserRefreshToken(newAccessToken, userId);
        userRefreshTokenRedisService.updateUsedUserRefreshToken(token);


        RefreshTokenResponse resData = RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        return resData;
    }
    public RefreshEmployeeTokenResponse refreshEmployeeToken(String refreshToken) throws Exception {
        DecodedJWT jwt =  jwtUtils.decodeRefreshToken(refreshToken);

        String userId = jwt.getSubject().toString();
        EmployeeToken token = employeeRefreshTokenRedisService.getInfoOfRefreshToken(refreshToken, userId);

        EmployeeCollection user = employeeService.exceptionIfNotExistedEmployeeById(userId);

        if (token == null) {
            throw new Exception(INVALID_TOKEN);
        }
        if(token.isUsed()) {
            employeeRefreshTokenRedisService.deleteUserRefreshToken(userId);
            throw new Exception(STOLEN_TOKEN);
        }

        List<String> roles = jwt.getClaim(ROLES_CLAIM_KEY).asList(String.class);

        String newAccessToken = jwtUtils.issueAccessToken(user.getId(), user.getUsername(), roles);
        String newRefreshToken = jwtUtils.issueRefreshToken(user.getId(), user.getUsername(), roles);
        employeeRefreshTokenRedisService.updateUsedEmployeeRefreshToken(token);
        employeeRefreshTokenRedisService.createNewEmployeeRefreshToken(refreshToken, userId);


        RefreshEmployeeTokenResponse resData = RefreshEmployeeTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        return resData;
    }
}
