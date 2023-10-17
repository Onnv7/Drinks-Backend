package com.hcmute.drink.service;

import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.dto.UpdatePasswordRequest;
import com.hcmute.drink.dto.UpdateUserRequest;

public interface UserService {
    public UserCollection findByEmail(String email);
    public UserCollection findUserById(String userId) throws Exception;
    public boolean updatePassword(String userId, UpdatePasswordRequest data) throws Exception;
    public UserCollection updateUser(String userId, UserCollection body) throws Exception;
}
