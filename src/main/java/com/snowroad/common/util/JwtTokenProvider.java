package com.snowroad.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long ACCESS_TOKEN_VALIDITY;
    private final long REFRESH_TOKEN_VALIDITY;

    public JwtTokenProvider(
            @Value("${secret.key}") String secretKey,
            @Value("${access.token.validity}") long accessTokenValidity,
            @Value("${refresh.token.validity}") long refreshTokenValidity
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.ACCESS_TOKEN_VALIDITY = accessTokenValidity;
        this.REFRESH_TOKEN_VALIDITY = refreshTokenValidity;
    }
    /**
     * Access Token 생성
     */
    public String generateAccessToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("type", "access")
                .claim("userId", userId)
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
                .claim("type", "refresh")
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        return validateTokenWithType(token, "access");
    }

    public boolean validateRefreshToken(String token) {
        return validateTokenWithType(token, "refresh");
    }

    private boolean validateTokenWithType(String token, String expectedType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return !claims.getExpiration().before(new Date())
                    && expectedType.equals(claims.get("type"));
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
