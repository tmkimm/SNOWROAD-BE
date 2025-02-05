package com.snowroad.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "SSBjZGVmZ2hpc2Fzc2RhZGZyZmVnYnlvZ3NldHlja1RVd2FzZA=="; // ✅ 고정된 키 (32바이트 이상 추천)
    private final SecretKey secretKey;
    private static final long ACCESS_TOKEN_VALIDITY = 30 * 60 * 1000; // 30분
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7일
    public JwtTokenProvider() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)); // ✅ 하드코딩된 키 사용
    }
    /**
     * Access Token 생성
     */
    public String generateAccessToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 검증
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 토큰이 만료되지 않았는지 확인
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /** ✅ JWT에서 사용자 ID 추출 */
    public String getUserIdFromToken(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
    }
}
