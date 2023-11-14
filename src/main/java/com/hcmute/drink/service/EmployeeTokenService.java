package com.hcmute.drink.service;

import com.hcmute.drink.collection.EmployeeTokenCollection;
import com.hcmute.drink.collection.TokenCollection;
import com.hcmute.drink.repository.EmployeeTokenRepository;
import com.hcmute.drink.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeTokenService {
    private final EmployeeTokenRepository employeeTokenRepository;
    private final JwtUtils jwtUtils;

    public void createToken(String refreshToken, String userId) {
        EmployeeTokenCollection data = EmployeeTokenCollection.builder()
                .refreshToken(refreshToken)
                .used(false)
                .employeeId(new ObjectId(userId))
                .build();
        employeeTokenRepository.save(data);
    }
    public EmployeeTokenCollection findTokenById(String id) {
        return employeeTokenRepository.findById(id).orElse(null);
    }


    public TokenCollection findByRefreshToken(String token) {
        return employeeTokenRepository.findByRefreshToken(token);
    }
    public void deleteTokenById(String id) {
        employeeTokenRepository.deleteById(id);
    }

    public void deleteAllByUserId(String userId) {
        employeeTokenRepository.deleteByEmployeeId(new ObjectId(userId));
    }

    public void updateTokenIsUsed(String id) {
        EmployeeTokenCollection token = employeeTokenRepository.findById(id).orElseThrow();
        token.setUsed(true);
        employeeTokenRepository.save(token);
    }
}
