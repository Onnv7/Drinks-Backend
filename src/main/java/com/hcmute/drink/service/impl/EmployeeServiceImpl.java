package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.dto.LoginResponse;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl {
    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;
    private final JwtUtils jwtIssuer;
    @Autowired
    @Qualifier("modelMapperNotNull")
    private ModelMapper modelMapperNotNull;

    public List<EmployeeCollection> getAllEmployees() throws Exception {
        return employeeRepository.findAll();
    }
    public EmployeeCollection getEmployeeById(String id) throws Exception {
        return checkExistedEmployee(id);
    }

    public EmployeeCollection findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    public void registerEmployee(EmployeeCollection data) throws Exception {
        EmployeeCollection existedEmployee = employeeRepository.findByUsername(data.getUsername());
        if (existedEmployee != null) {
            throw new Exception(ErrorConstant.REGISTERED_EMAIL);
        }
        EmployeeCollection newEmployee = employeeRepository.save(data);
        if (newEmployee == null) {
            throw new Exception(ErrorConstant.CREATED_FAILED);
        }
    }

    public LoginResponse attemptEmployeeLogin(String email, String password) throws Exception {
        UserPrincipal principal = UserPrincipal.builder()
                .username(email)
                .password(password)
                .build();
        Authentication employeeCredential = new EmployeeUsernamePasswordAuthenticationToken(principal);
        var authentication = authenticationManager.authenticate(employeeCredential);

        var principalAuthenticated = (UserPrincipal) authentication.getPrincipal();
        EmployeeCollection employee = employeeRepository.findByUsername(principalAuthenticated.getUsername());

        if(!employee.isEnabled()) {
           throw new Exception(ErrorConstant.ACCOUNT_BLOCKED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var roles = principalAuthenticated.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();

        var token = jwtIssuer.issue(principalAuthenticated.getUserId(), principalAuthenticated.getUsername(), roles);
        return LoginResponse.builder().accessToken(token).build();
    }

    public EmployeeCollection updateEmployee(EmployeeCollection data) throws Exception {
        EmployeeCollection employee = checkExistedEmployee(data.getId());
        modelMapperNotNull.map(data, employee);
        return employeeRepository.save(employee);
    }

    public void deleteEmployeeById(String id) throws Exception {
        checkExistedEmployee(id);
        employeeRepository.deleteById(id);
    }

    private EmployeeCollection checkExistedEmployee(String id) throws Exception {

        EmployeeCollection  employee = employeeRepository.findById(id).orElse(null);
        if(employee == null) {
            throw new Exception(ErrorConstant.EMPLOYEE_NOT_FOUND);
        }
        return employee;
    }
}
