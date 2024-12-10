package com.snowroad.service;

import com.snowroad.domain.events.Events;
import com.snowroad.domain.events.EventsRepository;
import com.snowroad.web.dto.AdminLoginResponseDTO;
import com.snowroad.web.dto.EventsListResponseDto;
import com.snowroad.web.dto.EventsResponseDto;
import com.snowroad.web.dto.EventsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
