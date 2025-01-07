package com.snowroad.common.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String jwtSecretKey;


    public String generateAccessToken(String id, String userRole) {
        return this.generateToken(id, userRole, 3600);  // 1시간 만료
    }

    public String generateRefreshToken(String id, String userRole) {
        return this.generateToken(id, userRole, 604800);  // 7일 만료
    }

    private String generateToken(String userNo, String userRole, long expirationTimeInSeconds) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeInSeconds * 1000);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userNo",userNo);
        claims.put("userRole", userRole);


        return Jwts.builder()
                .setSubject(userNo)
                .setClaims(claims)  // userNo, userRole 등의 정보를 claims에 담아서 설정
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, this.jwtSecretKey)
                .compact();
    }
}

