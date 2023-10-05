package com.hcmute.drink.controller;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.CreateEmployeeRequest;
import com.hcmute.drink.dto.LoginResponse;
import com.hcmute.drink.dto.UpdateEmployeeRequest;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.impl.AuthServiceImpl;
import com.hcmute.drink.service.impl.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static com.hcmute.drink.constant.RouterConstant.*;
import static com.hcmute.drink.constant.SwaggerConstant.*;

@Tag(name = EMPLOYEE_CONTROLLER_TITLE)
@RestController
@RequestMapping(EMPLOYEE_BASE_PATH)
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImpl employeeService;
    private final ModelMapper modelMapper;

    @Operation(summary = EMPLOYEE_GET_ALL_SUM, description = EMPLOYEE_GET_ALL_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = EMPLOYEE_GET_ALL_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAllEmployees() {
        try {
            List<EmployeeCollection> data = employeeService.getAllEmployees();

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(data)
                    .message(SuccessConstant.GET)
                    .build();

            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = EMPLOYEE_GET_BY_ID_SUM, description = EMPLOYEE_GET_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.GET, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @GetMapping(path = EMPLOYEE_GET_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getEmployeeById(@PathVariable("employeeId") String id) {
        try {
            EmployeeCollection data = employeeService.getEmployeeById(id);

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(data)
                    .message(SuccessConstant.GET)
                    .build();

            return new ResponseEntity<>(res, StatusCode.OK);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = EMPLOYEE_REGISTER_SUM, description = EMPLOYEE_REGISTER_DES)
    @ApiResponse(responseCode = StatusCode.CODE_CREATED, description = SuccessConstant.CREATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = EMPLOYEE_REGISTER_SUB_PATH)
    public ResponseEntity<ResponseAPI> registerEmployee(@RequestBody @Validated CreateEmployeeRequest body) {
        try {
            EmployeeCollection data = modelMapper.map(body, EmployeeCollection.class);
            employeeService.registerEmployee(data);

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.CREATED)
                    .build();

            return new ResponseEntity<>(res, StatusCode.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = EMPLOYEE_LOGIN_SUM, description = EMPLOYEE_LOGIN_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.LOGIN, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PostMapping(path = EMPLOYEE_LOGIN_SUB_PATH)
    public ResponseEntity<ResponseAPI> loginEmployee(@RequestBody @Validated CreateEmployeeRequest body) {
        try {
            LoginResponse data = employeeService.attemptEmployeeLogin(body.getUsername(), body.getPassword());

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(data)
                    .message(SuccessConstant.LOGIN)
                    .build();

            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = EMPLOYEE_UPDATE_BY_ID_SUM, description = EMPLOYEE_UPDATE_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.UPDATED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @PutMapping(path = EMPLOYEE_UPDATE_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> updateEmployeeById(@PathVariable("employeeId") String id, @RequestBody @Validated  UpdateEmployeeRequest body) {
        try {
            EmployeeCollection data = modelMapper.map(body, EmployeeCollection.class);
            data.setId(id);
            EmployeeCollection dataUpdated = employeeService.updateEmployee(data);

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .data(dataUpdated)
                    .message(SuccessConstant.UPDATED)
                    .build();

            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = EMPLOYEE_DELETE_BY_ID_SUM, description = EMPLOYEE_DELETE_BY_ID_DES)
    @ApiResponse(responseCode = StatusCode.CODE_OK, description = SuccessConstant.DELETED, content = @Content(mediaType = JSON_MEDIA_TYPE))
    @DeleteMapping(path = EMPLOYEE_DELETE_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> deleteEmployeeById(@PathVariable("employeeId") String id) {
        try {
            employeeService.deleteEmployeeById(id);

            ResponseAPI res = ResponseAPI.builder()
                    .timestamp(new Date())
                    .message(SuccessConstant.DELETED)
                    .build();

            return new ResponseEntity<>(res, StatusCode.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

