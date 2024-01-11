package com.hcmute.drink.service.database;

import com.hcmute.drink.dto.request.ChangePasswordEmployeeRequest;
import com.hcmute.drink.dto.request.CreateEmployeeRequest;
import com.hcmute.drink.dto.request.UpdateEmployeeRequest;
import com.hcmute.drink.dto.request.UpdatePasswordEmployeeRequest;
import com.hcmute.drink.dto.response.GetAllEmployeeResponse;
import com.hcmute.drink.dto.response.GetEmployeeByIdResponse;
import com.hcmute.drink.dto.response.UpdateEmployeeResponse;

import java.util.List;

public interface IEmployeeService {

    List<GetAllEmployeeResponse> getAllEmployees();
    GetEmployeeByIdResponse getEmployeeById(String id);
    void registerEmployee(CreateEmployeeRequest body);
    UpdateEmployeeResponse updateEmployee(UpdateEmployeeRequest data, String id);
    void updatePasswordByAdmin(UpdatePasswordEmployeeRequest data, String id);
    void deleteEmployeeById(String id);
    void changePasswordProfile(ChangePasswordEmployeeRequest data, String emplId);
}
