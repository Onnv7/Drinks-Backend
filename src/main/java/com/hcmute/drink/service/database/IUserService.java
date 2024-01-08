package com.hcmute.drink.service;

import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.dto.request.UpdatePasswordRequest;
import com.hcmute.drink.dto.request.UpdateUserRequest;
import com.hcmute.drink.dto.response.GetAllUserResponse;
import com.hcmute.drink.dto.response.GetUserByIdResponse;
import com.hcmute.drink.dto.response.UpdateUserResponse;

import java.util.List;

public interface IUserService {
    List<GetAllUserResponse> getAllUsers();
    GetUserByIdResponse getUserProfileById(String userId);
    void changePasswordProfile(String userId, UpdatePasswordRequest data);
    String checkExistedUserByEmail(String email);
    UpdateUserResponse updateUserProfile(String userId, UpdateUserRequest body);
}
