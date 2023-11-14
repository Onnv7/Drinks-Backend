package com.hcmute.drink.service;

import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.GetAllUserResponse;
import com.hcmute.drink.dto.GetUserByIdResponse;
import com.hcmute.drink.dto.UpdatePasswordRequest;
import com.hcmute.drink.repository.UserRepository;
import com.hcmute.drink.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserCollection findByEmail(String email) {
        UserCollection user = userRepository.findByEmail(email);
        return user;
    }
    public UserCollection findById(String id) {
        UserCollection user = userRepository.findById(id).orElse(null);
        return user;
    }
    // UTILS =================================================================

    public UserCollection exceptionIfNotExistedUserById(String id) throws Exception {
        UserCollection user = userRepository.findById(id).orElse(null);
        if(user == null) {
            throw new Exception(ErrorConstant.USER_NOT_FOUND + " with user id " + id);
        }
        return user;
    }
    public UserCollection exceptionIfNotExistedUserByEmail(String email) throws Exception {
        UserCollection user = userRepository.findByEmail(email);
        if(user == null) {
            throw new Exception(ErrorConstant.USER_NOT_FOUND);
        }
        return user;
    }

    // SERVICE =================================================================
    public List<GetAllUserResponse> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public GetUserByIdResponse getUserProfileById(String userId) throws Exception {
        securityUtils.exceptionIfNotMe(userId);
        GetUserByIdResponse user = userRepository.getUserProfileById(userId);
        if (user == null) {
            throw new Exception(ErrorConstant.USER_NOT_FOUND);
        }
        return user;
    }

    public void changePasswordProfile(String userId, UpdatePasswordRequest data) throws Exception {

        securityUtils.exceptionIfNotMe(userId);
        UserCollection user = exceptionIfNotExistedUserById(userId);

        boolean isValid = passwordEncoder.matches(data.getOldPassword(), user.getPassword());

        if(!isValid) {
            throw new Exception(ErrorConstant.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(data.getNewPassword()));
        userRepository.save(user);
    }

    public void changePasswordForgot(String email, String password) throws Exception {
        UserCollection user = exceptionIfNotExistedUserByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }


    public UserCollection updateUserProfile(String userId, UserCollection body) throws Exception {
        securityUtils.exceptionIfNotMe(userId);
        UserCollection user = exceptionIfNotExistedUserById(userId);
        modelMapperNotNull.map(body, user);
        user.setUpdatedAt(new Date());
        UserCollection updatedUser = userRepository.save(user);
        return updatedUser;
    }
    public String checkExistedUserByEmail(String email) throws Exception {
        UserCollection user = exceptionIfNotExistedUserByEmail(email);
        return user.getLastName() + user.getFirstName();
    }

}
