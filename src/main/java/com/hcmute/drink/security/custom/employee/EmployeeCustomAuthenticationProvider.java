package com.hcmute.drink.security.custom.employee;

import com.hcmute.drink.security.UserPrincipal;
import com.hcmute.drink.security.custom.user.UserUsernamePasswordAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeCustomAuthenticationProvider implements AuthenticationProvider
{
    private final CustomEmployeeDetailsService employeeDetailsService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String username = ((UserPrincipal) authentication.getPrincipal()).getUsername();
        String password = ((UserPrincipal) authentication.getPrincipal()).getPassword();

        if(username == null || password == null) {
            throw new BadCredentialsException("No pre-authenticated credentials found in request.");
        }
        UserPrincipal emplPrincipal = (UserPrincipal) employeeDetailsService.loadUserByUsername(username);
        if(passwordEncoder.matches(password, emplPrincipal.getPassword())) {
            return new UserUsernamePasswordAuthenticationToken(emplPrincipal, emplPrincipal.getPassword(), emplPrincipal.getAuthorities());
        }
        else {
            throw new BadCredentialsException("Authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(EmployeeUsernamePasswordAuthenticationToken.class);
    }
}