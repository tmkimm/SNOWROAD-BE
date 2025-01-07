package com.snowroad.admin.service;

import com.snowroad.admin.web.dto.AdminLoginResponseDTO;
import com.snowroad.common.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AdminService {



    private final TokenService tokenService;
    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;
    public AdminLoginResponseDTO login(String id, String password) {
        String accessToken;
        String refreshToken;
        if (authenticate(id, password)) {
            accessToken = tokenService.generateAccessToken(id, "admin");
            refreshToken = tokenService.generateRefreshToken(id, "admin");
        } else {
            throw new RuntimeException("Authentication failed");
        }
        return new AdminLoginResponseDTO(accessToken, refreshToken);
    }


    private boolean authenticate(String id, String password) {
        if(Objects.equals(id, adminUsername) && Objects.equals(password, adminPassword)){
            return true;
        }
        else
        {
            return false;
        }
    }
}
