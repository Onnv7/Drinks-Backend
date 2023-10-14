package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.TokenCollection;
import com.hcmute.drink.repository.TokenRepository;
import com.hcmute.drink.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl {

    private final TokenRepository tokenRepository;
    private final JwtUtils jwtUtils;

    public void createToken(String refreshToken, String userId) {
        TokenCollection data = TokenCollection.builder()
                .refreshToken(refreshToken)
                .enable(true)
                .userId(new ObjectId(userId))
                .build();
        tokenRepository.save(data);
    }
}
