package com.snowroad.service;

import com.snowroad.domain.events.Events;
import com.snowroad.domain.events.EventsRepository;
import com.snowroad.web.dto.AdminLoginResponseDTO;
import com.snowroad.web.dto.EventsResponseDto;
import com.snowroad.web.dto.EventsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final EventsRepository eventsRepository;

    @Transactional
    public Long save(EventsSaveRequestDto requestDto) {
        return eventsRepository.save(requestDto.toEntity()).getEventId();
    }

    @Transactional
    public Long update(Long id, EventsSaveRequestDto requestDto) {
        Events events = eventsRepository.findById(id)
                .orElseThrow(() -> new
        IllegalArgumentException("이벤트가 존재하지 않습니다. id" + id));

        events.update(requestDto.getEventNm(), requestDto.getEventCntn(), requestDto.getEventAddr(), requestDto.getOperStatDt(), requestDto.getOperEndDt(), requestDto.getOperDttmCntn(), requestDto.getCtgyId(), requestDto.getPpstEnbnTypeCd(), requestDto.getAddrLttd(), requestDto.getAddrLotd());
        return id;
    }

    public EventsResponseDto findById(Long id) {
        Events entity = eventsRepository.findById(id)
                .orElseThrow(() -> new
                        IllegalArgumentException("이벤트가 존재하지 않습니다. id" + id));
        return new EventsResponseDto(entity);
    }

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
