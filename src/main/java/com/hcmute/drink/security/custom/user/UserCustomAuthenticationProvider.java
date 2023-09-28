package com.hcmute.drink.security.custom.user;

import com.hcmute.drink.security.UserPrincipal;
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
public class UserCustomAuthenticationProvider implements AuthenticationProvider
{
    private final CustomUserDetailsService customUserDetailsService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String username = ((UserPrincipal) authentication.getPrincipal()).getUsername();
        String password = ((UserPrincipal) authentication.getPrincipal()).getPassword();
        if(username == null || password == null) {
            throw new BadCredentialsException("No pre-authenticated credentials found in request.");
        }
        // check username and password != null  nếu null thì  throw new BadCredentialsException("No pre-authenticated credentials found in request.");
        UserPrincipal userPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername(username);
        if(passwordEncoder.matches(password, userPrincipal.getPassword())) {
            return new UserUsernamePasswordAuthenticationToken(userPrincipal, userPrincipal.getPassword(), userPrincipal.getAuthorities());
        }
        else {
            throw new BadCredentialsException("Authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UserUsernamePasswordAuthenticationToken.class);
    }
}