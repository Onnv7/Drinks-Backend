package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.request.UpdateUserRequest;
import com.hcmute.drink.dto.response.GetAllUserResponse;
import com.hcmute.drink.dto.response.GetUserByIdResponse;
import com.hcmute.drink.dto.request.UpdatePasswordRequest;
import com.hcmute.drink.dto.response.UpdateUserResponse;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.UserRepository;
import com.hcmute.drink.service.database.IUserService;
import com.hcmute.drink.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final ModelMapperUtils modelMapperUtils;
    private final UserRepository userRepository;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserCollection findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    public UserCollection saveUser(UserCollection user) {
        return userRepository.save(user);
    }

    public UserCollection findById(String id) {
        UserCollection user = userRepository.findById(id).orElse(null);
        return user;
    }

    public UserCollection getById(String id) {
        return  userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }

    public UserCollection getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + email));
    }

    // SERVICE =================================================================
    @Override
    public List<GetAllUserResponse> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public GetUserByIdResponse getUserProfileById(String userId) {
        GetUserByIdResponse user = userRepository.getUserProfileById(userId);
        if (user == null) {
            throw new CustomException(ErrorConstant.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public void changePasswordProfile(String userId, UpdatePasswordRequest data) {

        UserCollection user = getById(userId);

        boolean isValid = passwordEncoder.matches(data.getOldPassword(), user.getPassword());

        if (!isValid) {
            throw new CustomException(ErrorConstant.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(data.getNewPassword()));
        userRepository.save(user);
    }

    public void changePasswordForgot(String email, String password) throws Exception {
        UserCollection user = getByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public UpdateUserResponse updateUserProfile(String userId, UpdateUserRequest body) {
        UserCollection data = modelMapperUtils.mapClass(body, UserCollection.class);
        UserCollection user = getById(userId);
        modelMapperUtils.map(data, user);
        user.setUpdatedAt(new Date());
        UserCollection updatedUser = userRepository.save(user);
        return modelMapperUtils.mapClass(updatedUser, UpdateUserResponse.class);
    }

    @Override
    public String checkExistedUserByEmail(String email) {
        UserCollection user = getByEmail(email);
        return user.getLastName() + user.getFirstName();
    }

}
