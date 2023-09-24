package com.hcmute.drink.controller;


import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.ChangePasswordRequest;
import com.hcmute.drink.dto.UpdateUserRequest;
import com.hcmute.drink.dto.UpdateUserResponse;
import com.hcmute.drink.model.ApiResponse;
import com.hcmute.drink.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;


    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable String userId) {
        try {
            UserCollection user = userService.findUserById(userId);
            ApiResponse res = ApiResponse.builder()
                    .message(SuccessConstant.GET_USER)
                    .data(user)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/change-password/{userId}")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable String userId,
                                                      @RequestBody @Validated ChangePasswordRequest body) {
        try {
            userService.updatePassword(userId, body.getPassword());
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .success(true)
                    .message(SuccessConstant.UPDATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable("userId") String userId,
                                                  @RequestBody @Validated UpdateUserRequest body
    ) {
        try {
            UserCollection data = userService.updateUser(userId, body);
            UpdateUserResponse resData = new UpdateUserResponse();
            modelMapper.map(data, resData);
            ApiResponse res = ApiResponse.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.UPDATED)
                    .data(resData)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
