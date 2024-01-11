package com.hcmute.drink.command;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.enums.Role;
import com.hcmute.drink.service.database.implement.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateAdminAccountCommand implements CommandLineRunner {
    private final EmployeeService employeeService;
    @Value("${app.redis_host}")
    private String redis_host;

    @Value("${app.username_admin}")
    private String username;
    @Value("${app.password_admin}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        EmployeeCollection existedEmployee = employeeService.findByUsername("admin01");
        if (existedEmployee != null) {
            log.info("ADMIN account existed");
           return;
        }
        EmployeeCollection admin = EmployeeCollection.builder()
                .username(username)
                .password(password)
                .roles(new Role[]{Role.ROLE_ADMIN, Role.ROLE_EMPLOYEE })
                        .build();
        log.info("ADMIN account created");
        employeeService.save(admin);
    }
}
