package com.hcmute.drink.service;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.ChangePasswordEmployeeRequest;
import com.hcmute.drink.dto.GetAllEmployeeResponse;

import com.hcmute.drink.dto.UpdateEmployeeRequest;
import com.hcmute.drink.dto.UpdatePasswordEmployeeRequest;
import com.hcmute.drink.repository.EmployeeRepository;
import com.hcmute.drink.utils.JwtUtils;
import com.hcmute.drink.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final TokenService tokenService;


    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final SecurityUtils securityUtils;
    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;

    public EmployeeCollection exceptionIfNotExistedEmployeeById(String id) throws Exception {
        EmployeeCollection  employee = employeeRepository.findById(id).orElse(null);
        if(employee == null) {
            throw new Exception(ErrorConstant.EMPLOYEE_NOT_FOUND);
        }
//        employee.setPassword(null);
        return employee;
    }

    public EmployeeCollection getEmployeeByIdNotException(String id)  {
        EmployeeCollection  employee = employeeRepository.findById(id).orElse(null);

        return employee;
    }
    // SERVICES =================================================================

    public List<GetAllEmployeeResponse> getAllEmployees() throws Exception {
        return employeeRepository.getAllEmployees();
    }
    public EmployeeCollection getEmployeeById(String id) throws Exception {
        return exceptionIfNotExistedEmployeeById(id);
    }

    public EmployeeCollection findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    public void registerEmployee(EmployeeCollection data) throws Exception {
        EmployeeCollection existedEmployee = employeeRepository.findByUsername(data.getUsername());
        if (existedEmployee != null) {
            throw new Exception(ErrorConstant.REGISTERED_EMAIL);
        }
        data.setPassword(passwordEncoder.encode(data.getPassword()));
        EmployeeCollection newEmployee = employeeRepository.save(data);
        if (newEmployee == null) {
            throw new Exception(ErrorConstant.CREATED_FAILED);
        }
    }


    public void updatePasswordByAdmin(UpdatePasswordEmployeeRequest data, String id) throws Exception {
        EmployeeCollection employee = exceptionIfNotExistedEmployeeById(id);
        employee.setPassword(passwordEncoder.encode(data.getPassword()));
        employeeRepository.save(employee);
    }
    public EmployeeCollection updateEmployee(UpdateEmployeeRequest data, String id) throws Exception {
        EmployeeCollection employee = exceptionIfNotExistedEmployeeById(id);
        modelMapperNotNull.map(data, employee);
        return employeeRepository.save(employee);
    }

    public void deleteEmployeeById(String id) throws Exception {
        exceptionIfNotExistedEmployeeById(id);
        employeeRepository.deleteById(id);
    }


    public void changePasswordProfile(ChangePasswordEmployeeRequest data, String emplId) throws Exception {
        securityUtils.exceptionIfNotMe(emplId);
        EmployeeCollection employee = exceptionIfNotExistedEmployeeById(emplId);
        boolean isValid = passwordEncoder.matches(data.getOldPassword(), employee.getPassword());
        if(!isValid) {
            throw new Exception(ErrorConstant.INVALID_PASSWORD);
        }
        employee.setPassword(passwordEncoder.encode(data.getNewPassword()));
        employeeRepository.save(employee);
    }
}
