package com.hcmute.drink.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hcmute.drink.config.JwtProperties;
import com.hcmute.drink.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    public static final String EMAIL_CLAIM_KEY = "email";
    public static final String ROLES_CLAIM_KEY = "roles";
    private final JwtProperties properties;

    public String issueAccessToken(String userId, String email, List<String> roles) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(30, ChronoUnit.DAYS)))
                .withClaim(EMAIL_CLAIM_KEY, email)
                .withClaim(ROLES_CLAIM_KEY, roles)
                .sign(Algorithm.HMAC256(properties.getAccessTokenKey()));
    }
    public String issueRefreshToken(String userId, String email, List<String> roles) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(7, ChronoUnit.DAYS)))
                .withClaim(EMAIL_CLAIM_KEY, email)
                .withClaim(ROLES_CLAIM_KEY, roles)
                .sign(Algorithm.HMAC256(properties.getRefreshTokenKey()));
    }

    public UserPrincipal convert(DecodedJWT jwt) {
        return UserPrincipal.builder()
                .userId(jwt.getSubject())
                .username(jwt.getClaim(EMAIL_CLAIM_KEY).asString())
                .authorities(extractAuthoritiesFromClaim(jwt))
                .build();

    }
    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT jwt) {
        var claim = jwt.getClaim(ROLES_CLAIM_KEY);
        if(claim.isNull() && claim.isMissing()) return List.of();
        return claim.asList(SimpleGrantedAuthority.class);
    }
    public DecodedJWT decodeAccessToken(String token) {
        return JWT.require(Algorithm.HMAC256(properties.getAccessTokenKey()))
                .build()
                .verify(token);
    }
    public DecodedJWT decodeRefreshToken(String token) {
        return JWT.require(Algorithm.HMAC256(properties.getRefreshTokenKey()))
                .build()
                .verify(token);
    }
}
