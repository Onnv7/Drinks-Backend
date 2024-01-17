package com.hcmute.drink.controller;

import com.hcmute.drink.constant.StatusCode;
import com.hcmute.drink.constant.SuccessConstant;
import com.hcmute.drink.dto.request.ChangePasswordEmployeeRequest;
import com.hcmute.drink.dto.request.CreateEmployeeRequest;
import com.hcmute.drink.dto.request.UpdateEmployeeRequest;
import com.hcmute.drink.dto.request.UpdatePasswordEmployeeRequest;
import com.hcmute.drink.dto.response.GetAllEmployeeResponse;
import com.hcmute.drink.dto.response.GetEmployeeByIdResponse;
import com.hcmute.drink.dto.response.UpdateEmployeeForAdminResponse;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.service.database.IEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final IEmployeeService employeeService;

    @Operation(summary = EMPLOYEE_GET_ALL_SUM)
    @GetMapping(path = GET_EMPLOYEE_ALL_SUB_PATH)
    public ResponseEntity<ResponseAPI> getAllEmployees(
            @Parameter(name = "key", description = "Key is order's id, customer name or phone number", required = false, example = "65439a55e9818f43f8b8e02c")
            @RequestParam(name = "key", required = false) String key,
            @Parameter(name = "page", required = true, example = "1")
            @RequestParam("page") @Min(value = 1, message = "Page must be greater than 0") int page,
            @Parameter(name = "size", required = true, example = "10")
            @RequestParam("size") @Min(value = 1, message = "Size must be greater than 0") int size
    ) {
        List<GetAllEmployeeResponse> resData = employeeService.getAllOrSearchByKey(key, page, size);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resData)
                .message(SuccessConstant.GET)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = EMPLOYEE_GET_BY_ID_SUM)
    @GetMapping(path = GET_EMPLOYEE_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> getEmployeeById(@PathVariable(EMPLOYEE_ID) String id) {
        GetEmployeeByIdResponse resData = employeeService.getEmployeeById(id);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resData)
                .message(SuccessConstant.GET)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = EMPLOYEE_REGISTER_SUM)
    @PostMapping(path = POST_EMPLOYEE_REGISTER_SUB_PATH)
    public ResponseEntity<ResponseAPI> registerEmployee(@RequestBody @Valid CreateEmployeeRequest body) {
        employeeService.registerEmployee(body);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.CREATED)
                .build();

        return new ResponseEntity<>(res, StatusCode.CREATED);
    }


    @Operation(summary = EMPLOYEE_UPDATE_BY_ID_SUM)
    @PutMapping(path = PUT_EMPLOYEE_UPDATE_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> updateEmployeeForAdmin(@PathVariable(EMPLOYEE_ID) String id, @RequestBody @Valid UpdateEmployeeRequest body) {
        UpdateEmployeeForAdminResponse resData = employeeService.updateEmployeeForAdmin(body, id);
        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .data(resData)
                .message(SuccessConstant.UPDATED)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = EMPLOYEE_UPDATE_PASSWORD_BY_ID_SUM)
    @PatchMapping(path = PATCH_EMPLOYEE_UPDATE_PASSWORD_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> changePasswordByAdmin(@PathVariable(EMPLOYEE_ID) String id, @RequestBody @Valid UpdatePasswordEmployeeRequest body) {
        employeeService.updatePasswordByAdmin(body, id);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.UPDATED)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = EMPLOYEE_DELETE_BY_ID_SUM)
    @DeleteMapping(path = DELETE_EMPLOYEE_BY_ID_SUB_PATH)
    public ResponseEntity<ResponseAPI> deleteEmployeeById(@PathVariable(EMPLOYEE_ID) String id) {
        employeeService.deleteEmployeeById(id);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.DELETED)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

    @Operation(summary = EMPLOYEE_UPDATE_PASSWORD_SUM)
    @PatchMapping(path = PATCH_EMPLOYEE_UPDATE_PASSWORD_SUB_PATH)
    public ResponseEntity<ResponseAPI> changePasswordProfile(@PathVariable(EMPLOYEE_ID) String id, @RequestBody @Valid ChangePasswordEmployeeRequest body) {
        employeeService.changePasswordProfile(body, id);

        ResponseAPI res = ResponseAPI.builder()
                .timestamp(new Date())
                .message(SuccessConstant.UPDATED)
                .build();

        return new ResponseEntity<>(res, StatusCode.OK);
    }

}

