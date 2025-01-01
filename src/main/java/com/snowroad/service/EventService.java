package com.snowroad.service;

import com.snowroad.domain.eventFilesDtl.EventFilesDtl;
import com.snowroad.domain.events.Events;
import com.snowroad.domain.events.EventsRepository;
import com.snowroad.web.dto.*;
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
    public void findAllDesc() {
//        eventsRepository.findAllDesc().stream()
//                .map(EventsListResponseDto::new)
//                .collect(Collectors.toList());

    }

    @Transactional
    public void delete(Long id) {
        Events events = eventsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 없습니다. id="+id));
        eventsRepository.delete(events);
    }

    // Event와 관련된 EventFilesDtl을 조회하는 서비스 메서드
    public List<EventsFileDetailResponseDTO> getEventFilesDtlList(Long eventId) {
        // Native Query 호출
        List<Object[]> result =  eventsRepository.findEventFilesDtlByEventId(eventId);
        // Object[]에서 데이터를 추출하여 필요한 형태로 가공
        List<EventsFileDetailResponseDTO> eventFiles = result.stream()
                .map(row -> {
                    EventsFileDetailResponseDTO fileDtl = new EventsFileDetailResponseDTO();
                    fileDtl.setFileDtlId((Long) row[0]);
                    fileDtl.setFileUrl((String) row[1]);
                    fileDtl.setOrigFileNm((String) row[2]);
                    return fileDtl;
                })
                .collect(Collectors.toList());

        return eventFiles;
    }
}
