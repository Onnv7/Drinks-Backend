package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.ChangePasswordEmployeeRequest;
import com.hcmute.drink.dto.LoginResponse;

import com.hcmute.drink.dto.UpdatePasswordEmployeeRequest;
import com.hcmute.drink.repository.EmployeeRepository;
import com.hcmute.drink.security.UserPrincipal;
import com.hcmute.drink.security.custom.employee.EmployeeUsernamePasswordAuthenticationToken;
import com.hcmute.drink.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl {
    private final EmployeeRepository employeeRepository;
    private final TokenServiceImpl tokenService;


    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;

    public List<EmployeeCollection> getAllEmployees() throws Exception {
        return employeeRepository.findAll();
    }
    public EmployeeCollection getEmployeeById(String id) throws Exception {
        return isExistedEmployeeOrException(id);
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


    public EmployeeCollection updatePassword(UpdatePasswordEmployeeRequest data, String id) throws Exception {
        EmployeeCollection employee = isExistedEmployeeOrException(id);
        employee.setPassword(passwordEncoder.encode(data.getPassword()));
        return employeeRepository.save(employee);
    }
    public EmployeeCollection updateEmployee(EmployeeCollection data) throws Exception {
        EmployeeCollection employee = isExistedEmployeeOrException(data.getId());
        modelMapperNotNull.map(data, employee);
        return employeeRepository.save(employee);
    }

    public void deleteEmployeeById(String id) throws Exception {
        isExistedEmployeeOrException(id);
        employeeRepository.deleteById(id);
    }

    public EmployeeCollection isExistedEmployeeOrException(String id) throws Exception {
        EmployeeCollection  employee = employeeRepository.findById(id).orElse(null);
        if(employee == null) {
            throw new Exception(ErrorConstant.EMPLOYEE_NOT_FOUND);
        }
//        employee.setPassword(null);
        return employee;
    }
    public void changePasswordEmployee(ChangePasswordEmployeeRequest data, String emplId) throws Exception {
        EmployeeCollection employee = isExistedEmployeeOrException(emplId);
        boolean isValid = passwordEncoder.matches(data.getOldPassword(), employee.getPassword());
        if(!isValid) {
            throw new Exception(ErrorConstant.INVALID_PASSWORD);
        }
        employee.setPassword(passwordEncoder.encode(data.getNewPassword()));
        employeeRepository.save(employee);
    }
}
