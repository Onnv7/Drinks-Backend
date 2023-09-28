package com.hcmute.drink.security.custom;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    @Autowired
    private AuthenticationManager authenticationManager;
    public MyAuthenticationFilter() {
        setAuthenticationManager(authenticationManager);
    }
    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        log.error("MyAuthenticationFilter authenticationManager");
        super.setAuthenticationManager(authenticationManager);
    }

}