package com.hcmute.drink.security;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@SecurityScheme(name = "nva", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomOidcUserService customOidcUserService;
    private static final String[] AUTH_WHITELIST = {
            "/openapi/**",
            "/v3/api-docs/**",
            "/openapi/swagger-config/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/auth/**",
            "/api/category/**",
            "/api/product/**",
            "/api/test/**"
    };
    private static final String[] USER_PATH = {
            "/api/user/**",
            "/api/product"
    };
    private static final String[] ADMIN_PATH = {
    };
    //                .requestMatchers("/v2/api-docs",
//                        "/api/users/**",
//                        "/v3/api-docs",
//                        "/v3/api-docs/**",
//                        "/swagger-resources",
//                        "/swagger-resources/**",
//                        "/configuration/ui",
//                        "/configuration/security",
//                        "/swagger-ui/**",
//                        "/webjars/**",
//                        "/swagger-ui.html"
//                        ).permitAll()

    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
        System.out.println("filter chain");
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .securityMatcher("/**")
                .authorizeHttpRequests(register -> register
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(USER_PATH).hasRole("USER")
                        .requestMatchers(ADMIN_PATH).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
//                .oauth2Login()
//                .defaultSuccessUrl("/api/test/ok")
//                .userInfoEndpoint()
//                .oidcUserService(customOidcUserService);

        ;

//                .securityMatcher("/**")
//                .authorizeHttpRequests(register -> register
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/pow-wow/v3/api-docs/**").permitAll()
//                        .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs và /webjars/** ").permitAll()
//                        .requestMatchers("/api/users/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("Pwd");
        return new BCryptPasswordEncoder();
    }
    // lắng nghe sự kiện auth gg thành công hoặc dùng Oidc
    @Bean
    ApplicationListener<AuthenticationSuccessEvent> doSomething() {
        return new ApplicationListener<AuthenticationSuccessEvent>() {
            @Override
            public void onApplicationEvent(AuthenticationSuccessEvent event){
                Authentication authentication = event.getAuthentication();
                // get required details from OAuth2Authentication instance and proceed further
            }
        };
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        System.out.println("Auth manager");
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}
