package com.snowroad.service;

import com.snowroad.domain.events.Events;
import com.snowroad.domain.events.EventsRepository;
import com.snowroad.web.dto.AdminLoginResponseDTO;
import com.snowroad.web.dto.EventsListResponseDto;
import com.snowroad.web.dto.EventsResponseDto;
import com.snowroad.web.dto.EventsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventService {
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

    @Transactional(readOnly = true)
    public List<EventsListResponseDto> findAllDesc() {
        return eventsRepository.findAllDesc().stream()
                .map(EventsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Events events = eventsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 없습니다. id="+id));
        eventsRepository.delete(events);
    }
}
