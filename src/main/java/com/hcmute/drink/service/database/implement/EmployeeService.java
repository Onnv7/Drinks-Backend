package com.hcmute.drink.service.database.implement;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.constant.ErrorConstant;

import com.hcmute.drink.dto.request.ChangePasswordEmployeeRequest;
import com.hcmute.drink.dto.request.CreateEmployeeRequest;
import com.hcmute.drink.dto.request.UpdateEmployeeRequest;
import com.hcmute.drink.dto.request.UpdatePasswordEmployeeRequest;
import com.hcmute.drink.dto.response.GetAllEmployeeResponse;
import com.hcmute.drink.dto.response.GetEmployeeByIdResponse;
import com.hcmute.drink.dto.response.UpdateEmployeeResponse;
import com.hcmute.drink.model.CustomException;
import com.hcmute.drink.repository.database.EmployeeRepository;
import com.hcmute.drink.service.database.IEmployeeService;
import com.hcmute.drink.utils.ModelMapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final SequenceService sequenceService;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;
    private final ModelMapperUtils modelMapperUtils;

    public EmployeeCollection getById(String id)  {
        return employeeRepository.findById(id).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + id));
    }

    // SERVICES =================================================================

    @Override
    public List<GetAllEmployeeResponse> getAllEmployees()  {
        return employeeRepository.getAllEmployees();
    }
    @Override
    public GetEmployeeByIdResponse getEmployeeById(String id) {
        EmployeeCollection employee =  getById(id);
        return modelMapperUtils.mapNotNullClass(employee, GetEmployeeByIdResponse.class);
    }

    public EmployeeCollection findByUsername(String username) {
        return employeeRepository.findByUsername(username).orElse(null);
    }
    public EmployeeCollection save(EmployeeCollection employee) {
        return employeeRepository.save(employee);
    }
    public EmployeeCollection getByUsername(String username) {
        return employeeRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorConstant.NOT_FOUND + username));
    }

    @Override
    @Transactional
    public void registerEmployee(CreateEmployeeRequest body)  {

        EmployeeCollection data = modelMapperUtils.mapClass(body, EmployeeCollection.class);
        EmployeeCollection existedEmployee = findByUsername(data.getUsername());
        if (existedEmployee != null) {
            throw new CustomException(ErrorConstant.REGISTERED_EMAIL);
        }
        data.setPassword(passwordEncoder.encode(data.getPassword()));
        data.setCode(sequenceService.generateCode(EmployeeCollection.SEQUENCE_NAME, EmployeeCollection.PREFIX_CODE, EmployeeCollection.LENGTH_NUMBER));
        employeeRepository.save(data);
//        throw new CustomException("43953405dfsdfsd");
    }
    @Override
    public void updatePasswordByAdmin(UpdatePasswordEmployeeRequest data, String id)  {
        EmployeeCollection employee = getById(id);
        employee.setPassword(passwordEncoder.encode(data.getPassword()));
        employeeRepository.save(employee);
    }
    @Override
    public UpdateEmployeeResponse updateEmployee(UpdateEmployeeRequest data, String id)  {
        EmployeeCollection employee = getById(id);
        modelMapperUtils.mapNotNull(data, employee);
        return modelMapperUtils.mapClass(employeeRepository.save(employee), UpdateEmployeeResponse.class);
    }

    @Override
    public void deleteEmployeeById(String id) {
        getById(id);
        employeeRepository.deleteById(id);
    }


    @Override
    public void changePasswordProfile(ChangePasswordEmployeeRequest data, String emplId)  {
        EmployeeCollection employee = getById(emplId);
        boolean isValid = passwordEncoder.matches(data.getOldPassword(), employee.getPassword());
        if(!isValid) {
            throw new CustomException(ErrorConstant.INVALID_PASSWORD);
        }
        employee.setPassword(passwordEncoder.encode(data.getNewPassword()));
        employeeRepository.save(employee);
    }
}
