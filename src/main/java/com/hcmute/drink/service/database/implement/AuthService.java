package com.hcmute.drink.service.database.implement;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hcmute.drink.collection.ConfirmationCollection;
import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.dto.kafka.CodeEmailDto;
import com.hcmute.drink.dto.request.*;
import com.hcmute.drink.dto.response.*;
import com.hcmute.drink.enums.EmployeeStatus;
import com.hcmute.drink.enums.Role;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.kafka.KafkaMessagePublisher;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.model.redis.EmployeeToken;
import com.hcmute.drink.model.redis.UserToken;
import com.hcmute.drink.repository.database.ConfirmationRepository;
import com.hcmute.drink.security.UserPrincipal;
import com.hcmute.drink.security.custom.employee.EmployeeUsernamePasswordAuthenticationToken;
import com.hcmute.drink.security.custom.user.UserUsernamePasswordAuthenticationToken;
import com.hcmute.drink.service.database.IAuthService;
import com.hcmute.drink.service.redis.EmployeeRefreshTokenRedisService;
import com.hcmute.drink.service.redis.UserRefreshTokenRedisService;
import com.hcmute.drink.utils.JwtUtils;
import com.hcmute.drink.service.common.ModelMapperService;
import com.hcmute.drink.utils.GeneratorUtils;
import lombok.RequiredArgsConstructor;
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
public class AuthService implements IAuthService {
    private final KafkaMessagePublisher kafkaMessagePublisher;
    private final ConfirmationRepository confirmationRepository;
    private final JwtUtils jwtUtils;
    private final SequenceService sequenceService;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationService confirmationService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final UserRefreshTokenRedisService userRefreshTokenRedisService;
    private final EmployeeRefreshTokenRedisService employeeRefreshTokenRedisService;
    private final ModelMapperService modelMapperService;

    @Override
    public LoginResponse userLogin(String email, String password) {
        UserPrincipal principal = UserPrincipal.builder()
                .username(email)
                .password(password)
                .build();

        Authentication userCredential = new UserUsernamePasswordAuthenticationToken(principal);
        var authentication = authenticationManager.authenticate(userCredential);

        var principalAuthenticated = (UserPrincipal) authentication.getPrincipal();
        UserCollection user = userService.findByEmail(principalAuthenticated.getUsername());

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

    @Override
    public EmployeeLoginResponse attemptEmployeeLogin(String username, String password) {
        UserPrincipal principal = UserPrincipal.builder()
                .username(username)
                .password(password)
                .build();
        Authentication employeeCredential = new EmployeeUsernamePasswordAuthenticationToken(principal);
        var authentication = authenticationManager.authenticate(employeeCredential);

        var principalAuthenticated = (UserPrincipal) authentication.getPrincipal();
        EmployeeCollection employee = employeeService.findByUsername(principalAuthenticated.getUsername());

        if (employee.getStatus() == EmployeeStatus.INACTIVE) {
            throw new CustomException(ErrorConstant.ACCOUNT_BLOCKED);
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

    @Override
    public RegisterResponse registerUser(RegisterUserRequest body) {

        UserCollection data = modelMapperService.mapClass(body, UserCollection.class);
        UserCollection existedUser = userService.findByEmail(data.getEmail());

        if (existedUser != null) {
            throw new CustomException(ErrorConstant.REGISTERED_EMAIL);
        }
        RegisterResponse resData = new RegisterResponse();
        modelMapperService.map(data, resData);

        data.setRoles(new Role[]{Role.ROLE_USER});
        data.setPassword(passwordEncoder.encode(data.getPassword()));
        data.setCode(sequenceService.generateCode(UserCollection.SEQUENCE_NAME, UserCollection.PREFIX_CODE, UserCollection.LENGTH_NUMBER));
        UserCollection savedUser = userService.saveUser(data);

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

    @Override
    public void resendCode(String email) {
        ConfirmationCollection confirmation = confirmationRepository.findByEmail(email);
        if (confirmation == null) {
            throw new CustomException(ErrorConstant.NOT_FOUND + email);
        }

        String code = GeneratorUtils.generateRandomCode(6);
        confirmation.setCode(code);
        confirmationService.updateConfirmationInfo(confirmation);
        kafkaMessagePublisher.sendMessageToCodeEmail(new CodeEmailDto(code, email));
    }


    @Override
    public void sendCodeToRegister(String email) {
        UserCollection user = userService.findByEmail(email);
        if (user != null) {
            throw new CustomException(ErrorConstant.REGISTERED_EMAIL);
        }
        String code = GeneratorUtils.generateRandomCode(6);
        confirmationService.createOrUpdateConfirmationInfo(email, code);
        kafkaMessagePublisher.sendMessageToCodeEmail(new CodeEmailDto(code, email));
    }

    @Override
    public void sendCodeToGetPassword(String email) {
        userService.getByEmail(email);
        String code = GeneratorUtils.generateRandomCode(6);
        confirmationService.createOrUpdateConfirmationInfo(email, code);
        kafkaMessagePublisher.sendMessageToCodeEmail(new CodeEmailDto(code, email));
    }

    @Override
    public void verifyCodeByEmail(String code, String email) {
        ConfirmationCollection confirmationCollection = confirmationRepository.findByEmail(email);
        if (confirmationCollection != null && code.equals(confirmationCollection.getCode())) {
            confirmationRepository.deleteById(confirmationCollection.getId());
            return;
        }
        if (confirmationCollection == null) {
            throw new CustomException(ErrorConstant.NOT_FOUND);
        }

        throw new CustomException(ErrorConstant.EMAIL_UNVERIFIED);
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        DecodedJWT jwt = jwtUtils.decodeRefreshToken(refreshToken);


        String userId = jwt.getSubject().toString();
        UserToken token = userRefreshTokenRedisService.getInfoOfRefreshToken(refreshToken, userId);

        UserCollection user = userService.getById(userId);

        if (token == null) {
            throw new CustomException(INVALID_TOKEN);
        }
        if (token.isUsed()) {
            userRefreshTokenRedisService.deleteUserRefreshToken(userId);
            throw new CustomException(STOLEN_TOKEN);
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

    @Override
    public RefreshEmployeeTokenResponse refreshEmployeeToken(String refreshToken)  {
        DecodedJWT jwt = jwtUtils.decodeRefreshToken(refreshToken);

        String userId = jwt.getSubject().toString();
        EmployeeToken token = employeeRefreshTokenRedisService.getInfoOfRefreshToken(refreshToken, userId);

        EmployeeCollection user = employeeService.getById(userId);

        if (token == null) {
            throw new CustomException(INVALID_TOKEN);
        }
        if (token.isUsed()) {
            employeeRefreshTokenRedisService.deleteUserRefreshToken(userId);
            throw new CustomException(STOLEN_TOKEN);
        }

        List<String> roles = jwt.getClaim(ROLES_CLAIM_KEY).asList(String.class);

        String newAccessToken = jwtUtils.issueAccessToken(user.getId(), user.getUsername(), roles);
        String newRefreshToken = jwtUtils.issueRefreshToken(user.getId(), user.getUsername(), roles);
        employeeRefreshTokenRedisService.updateUsedEmployeeRefreshToken(token);
        employeeRefreshTokenRedisService.createNewEmployeeRefreshToken(newRefreshToken, userId);


        RefreshEmployeeTokenResponse resData = RefreshEmployeeTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        return resData;
    }
}
