package com.hcmute.drink.security.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmute.drink.model.ErrorResponse;
import com.hcmute.drink.model.ResponseAPI;
import com.hcmute.drink.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static com.hcmute.drink.constant.ErrorConstant.EXPIRED_TOKEN;
import static com.hcmute.drink.constant.ErrorConstant.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            extractTokenFromRequest(request)
                    .map(jwtUtils::decodeAccessToken) // str -> jwtUtils.decodeAccessToken(str)  jwtUtils::decodeAccessToken
                    .map(jwtUtils::convert)
                    .map(UserPrincipalAuthenticationToken::new)
                    .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            String message = INVALID_TOKEN;
            if (e.getClass() == TokenExpiredException.class) {
                message = EXPIRED_TOKEN;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse resData = ErrorResponse.builder()
                    .message(message)
                    .timestamp(new Date())
                    .build();
            String jsonResponse = objectMapper.writeValueAsString(resData);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Ví dụ: 500 Internal Server Error
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);
        }
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}
