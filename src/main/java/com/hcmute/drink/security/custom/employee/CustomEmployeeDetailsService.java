package com.hcmute.drink.security.custom.employee;

import com.hcmute.drink.collection.EmployeeCollection;
import com.hcmute.drink.enums.Role;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.security.UserPrincipal;
import com.hcmute.drink.service.database.implement.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomEmployeeDetailsService implements UserDetailsService {
    private final EmployeeService employeeService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmployeeCollection user = employeeService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(ErrorConstant.USER_NOT_FOUND);
        }
//        else if(user.isVerifiedEmail() == false) {
//            throw new Exception(ErrorConstant.EMAIL_UNVERIFIED);
//        }

        List<String> roleNames = Arrays.stream(user.getRoles())
                .map(Role::name)
                .collect(Collectors.toList());

        List<SimpleGrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return UserPrincipal.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .authorities(authorities)
                .password(user.getPassword())
                .build();
    }
}
