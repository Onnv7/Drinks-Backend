package com.hcmute.drink.controller;


import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.UpdatePasswordRequest;
import com.hcmute.drink.dto.UpdateUserRequest;
import com.hcmute.drink.dto.UpdateUserResponse;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = USER_CONTROLLER_TITLE)
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    @Operation(summary = USER_GET_ALL_SUM, description = USER_GET_ALL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET_USER, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping()
    public ResponseEntity<ResponseAPI> getAllUser() {
        try {
//            SecurityContextHolder.getContext().getAuthentication().getCredentials();
            List<UserCollection> users = userService.getAllUsers();
            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.GET_USER)
                    .data(users)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Operation(summary = USER_GET_BY_ID_SUM, description = USER_GET_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET_USER, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseAPI> getUserById(@PathVariable String userId) {
        try {
            UserCollection user = userService.findUserById(userId);
            ResponseAPI res = ResponseAPI.builder()
                    .message(SuccessConstant.GET_USER)
                    .data(user)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Operation(summary = USER_CHANGE_PWD_SUM, description = USER_CHANGE_PWD_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.UPDATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PatchMapping("/change-password/{userId}")
    public ResponseEntity<ResponseAPI> changePassword(@PathVariable String userId,
                                                      @RequestBody @Validated UpdatePasswordRequest body) {
        try {
            userService.updatePassword(userId, body.getPassword());
            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .success(true)
                    .message(SuccessConstant.UPDATED)
                    .build();
            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = USER_UPDATE_BY_ID_SUM, description = USER_UPDATE_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.UPDATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PutMapping("/update/{userId}")
    public ResponseEntity<ResponseAPI> updateUser(@PathVariable("userId") String userId,
                                                  @RequestBody @Validated UpdateUserRequest body
    ) {
        try {
            UserCollection data = userService.updateUser(userId, body);
            UpdateUserResponse resData = new UpdateUserResponse();
            modelMapper.map(data, resData);
            ResponseAPI res = ResponseAPI.builder()
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
