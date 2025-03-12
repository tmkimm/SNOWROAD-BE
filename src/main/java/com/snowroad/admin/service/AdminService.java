package com.snowroad.admin.service;

import com.snowroad.admin.web.dto.AdminLoginResponseDTO;
import com.snowroad.common.exception.UnauthorizedException;
import com.snowroad.common.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AdminService {
    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.userid}")
    private String adminUserId;

    private final JwtTokenProvider jwtTokenProvider;

    public AdminLoginResponseDTO login(String id, String password) {
        String accessToken;
        String refreshToken;


        if (authenticate(id, password)) {
            accessToken = jwtTokenProvider.generateAccessToken(adminUserId);
            refreshToken = jwtTokenProvider.generateRefreshToken(adminUserId);
        } else {
            throw new UnauthorizedException("인증정보가 올바르지 않습니다.");
        }
        return new AdminLoginResponseDTO(accessToken, refreshToken);
    }


    private boolean authenticate(String id, String password) {
        return Objects.equals(id, adminUsername) && Objects.equals(password, adminPassword);
    }


}
