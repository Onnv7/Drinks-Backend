package com.hcmute.drink.service.impl;

import com.hcmute.drink.collection.TokenCollection;
import com.hcmute.drink.repository.TokenRepository;
import com.hcmute.drink.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl {

    private final TokenRepository tokenRepository;
    private final JwtUtils jwtUtils;

    public void createToken(String refreshToken, String userId) {
        TokenCollection data = TokenCollection.builder()
                .refreshToken(refreshToken)
                .used(false)
                .userId(new ObjectId(userId))
                .build();
        tokenRepository.save(data);
    }

    public TokenCollection findTokenById(String id) {
        return tokenRepository.findById(id).orElse(null);
    }


    public TokenCollection findByRefreshToken(String token) {
        return tokenRepository.findByRefreshToken(token);
    }
    public void deleteTokenById(String id) {
        tokenRepository.deleteById(id);
    }

    public void deleteAllByUserId(String userId) {
        tokenRepository.deleteByUserId(new ObjectId(userId));
//        List<TokenCollection> tokens = tokenRepository.findByUserId(userId);
//        for (TokenCollection token : tokens) {
//            tokenRepository.deleteById(token.getId());
//        }
    }

    public void updateTokenIsUsed(String id) {
       TokenCollection token = tokenRepository.findById(id).orElseThrow();
       token.setUsed(true);
       tokenRepository.save(token);
    }
}
