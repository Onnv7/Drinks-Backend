package com.hcmute.drink.command;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.enums.Role;
import com.hcmute.drink.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateAdminAccountCommand implements CommandLineRunner {
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) throws Exception {
        EmployeeCollection existedEmployee = employeeRepository.findByUsername("admin");
        if (existedEmployee != null) {
            log.info("ADMIN account existed");
           return;
        }
        EmployeeCollection admin = EmployeeCollection.builder()
                .username("admin")
                .password("$2a$07$v6ZbFt4l0Vy3XAzAhpEcGeccRJfk0qZK.PdeSqG821GKwPDsPYcvu")
                .roles(new Role[]{Role.ROLE_ADMIN, Role.ROLE_EMPLOYEE })
                        .build();
        log.info("ADMIN account created");
        employeeRepository.save(admin);

    }
}
