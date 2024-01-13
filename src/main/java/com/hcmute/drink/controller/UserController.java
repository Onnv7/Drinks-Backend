package com.hcmute.drink.controller;


import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.request.UpdatePasswordRequest;
import com.hcmute.drink.dto.request.UpdateUserRequest;
import com.hcmute.drink.dto.response.GetAllUserResponse;
import com.hcmute.drink.dto.response.GetUserByIdResponse;
import com.hcmute.drink.dto.response.UpdateUserResponse;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.database.implement.UserService;
import com.hcmute.drink.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = USER_CONTROLLER_TITLE)
@RestController
@RequestMapping(USER_BASE_PATH)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = USER_GET_ALL_SUM)
    @GetMapping(path = USER_GET_ALL_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAllUser() {
        List<GetAllUserResponse> resData = userService.getAllUsers();

        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.GET)
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

    @Operation(summary = USER_GET_BY_ID_SUM)
    @GetMapping(path = USER_GET_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getUserProfileById(@PathVariable(USER_ID) String userId) {
        GetUserByIdResponse resData = userService.getUserProfileById(userId);

        ResponseAPI res = ResponseAPI.builder()
                .message(SuccessConstant.GET)
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = USER_CHANGE_PWD_SUM)
    @PatchMapping(path = USER_CHANGE_PASSWORD_SUB_PATH)
    public ResponseEntity<ResponseAPI> changePasswordProfile(
            @PathVariable(USER_ID) String userId,
            @RequestBody @Valid UpdatePasswordRequest body
    ) {
        userService.changePasswordProfile(userId, body);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .success(true)
                .message(SuccessConstant.UPDATED)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = USER_UPDATE_BY_ID_SUM)
    @PutMapping(path = USER_UPDATE_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> updateUserProfile(
            @PathVariable(USER_ID) String userId,
            @RequestBody @Valid UpdateUserRequest body
    ) {
        UpdateUserResponse resData = userService.updateUserProfile(userId, body);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.UPDATED)
                .data(resData)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);

    }

    @Operation(summary = USER_CHECK_EXISTED_BY_EMAIL_SUM)
    @GetMapping(path = USER_CHECK_EXISTED_SUB_PATH)
    public ResponseEntity<ResponseAPI> checkExistedUserByEmail(@RequestParam("email") String email) {
        String result = userService.checkExistedUserByEmail(email);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.GET)
                .data(result)
                .build();
        return new ResponseEntity<>(res, StatusCode.OK);
    }

}
