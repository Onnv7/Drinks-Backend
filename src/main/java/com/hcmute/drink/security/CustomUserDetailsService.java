package com.hcmute.drink.security;

import com.hcmute.drink.collection.UserCollection;
import com.hcmute.drink.common.Role;
import com.hcmute.drink.constant.ErrorConstant;
import com.hcmute.drink.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
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
public class CustomUserDetailsService implements UserDetailsService {
    private final UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCollection user = userService.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(ErrorConstant.USER_NOT_FOUND);
        }

        List<String> roleNames = Arrays.stream(user.getRoles())
                .map(Role::name)
                .collect(Collectors.toList());

        List<SimpleGrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return UserPrincipal.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .authorities(authorities)
                .password(user.getPassword())
                .build();
    }
}
