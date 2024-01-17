package com.hcmute.drink.dto.response;

import com.hcmute.drink.enums.EmployeeStatus;
import com.hcmute.drink.enums.Gender;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GetAllEmployeeResponse {
    private int totalPage;
    private List<Employee> employeeList;

    @Data
    static class Employee {
        private String id;
        private String code;
        private String firstName;
        private String lastName;
        private String username;
        private Date birthDate;
        private Gender gender;
        private EmployeeStatus status;
    }

}
