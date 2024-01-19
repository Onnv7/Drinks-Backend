package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.ChangePasswordEmployeeRequest;
import com.hcmute.drink.dto.request.CreateEmployeeRequest;
import com.hcmute.drink.dto.request.UpdateEmployeeRequest;
import com.hcmute.drink.dto.request.UpdatePasswordEmployeeRequest;
import com.hcmute.drink.dto.response.GetAllEmployeeResponse;
import com.hcmute.drink.dto.response.GetEmployeeByIdResponse;
import com.hcmute.drink.dto.response.UpdateEmployeeForAdminResponse;
import com.hcmute.drink.enums.EmployeeStatus;

import java.util.List;

public interface IEmployeeService {

    List<GetAllEmployeeResponse> getEmployeeList(String key, int page, int size, EmployeeStatus status);
    GetEmployeeByIdResponse getEmployeeById(String id);
    void registerEmployee(CreateEmployeeRequest body);
    UpdateEmployeeForAdminResponse updateEmployeeForAdmin(UpdateEmployeeRequest data, String id);
    void updatePasswordByAdmin(UpdatePasswordEmployeeRequest data, String id);
    void deleteEmployeeById(String id);
    void changePasswordProfile(ChangePasswordEmployeeRequest data, String emplId);
}
