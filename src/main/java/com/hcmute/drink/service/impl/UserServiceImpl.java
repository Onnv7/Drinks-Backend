package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.UpdateUserRequest;
import com.hcmute.drink.repository.UserRepository;
import com.hcmute.drink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ModelMapper modelMapper;

    @Override
    public UserCollection findByEmail(String email) {
        UserCollection user = userRepository.findByEmail(email);
        return user;
    }

    public List<UserCollection> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserCollection findUserById(String userId) throws Exception {
        UserCollection result = new UserCollection();
        Optional<UserCollection> user = userRepository.findById(userId);
        if (user.isPresent()) {
            result = user.get();
            result.setPassword(null);
            return result;
        } else {
            throw new Exception(ErrorConstant.USER_NOT_FOUND);
        }
    }

    @Override
    public boolean updatePassword(String userId, String password) throws Exception {
        UserCollection result = new UserCollection();
        Optional<UserCollection> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new Exception(ErrorConstant.USER_NOT_FOUND);
        }
        result = user.get();
        result.setPassword(passwordEncoder.encode(password));
        if (userRepository.save(result) != null) {
            return true;
        }
        throw new Exception(ErrorConstant.CREATED_FAILED);
    }

    @Override
    public UserCollection updateUser(String userId, UpdateUserRequest body) throws Exception {
        UserCollection user = userRepository.findById(userId).orElseGet(() -> null);
        if (user == null) {
            throw new Exception(ErrorConstant.USER_NOT_FOUND);
        }
        modelMapper.map(body, user);
        user.setUpdatedAt(new Date());
        UserCollection updatedUser = userRepository.save(user);
        if(updatedUser != null) {
            updatedUser.setPassword(null);
            return updatedUser;
        }
        throw new Exception(ErrorConstant.UPDATE_FAILED);
    }
}
