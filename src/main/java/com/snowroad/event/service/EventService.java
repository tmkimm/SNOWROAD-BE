package com.snowroad.event.service;

import com.snowroad.admin.web.dto.AdminEventsListResponseDto;
import com.snowroad.event.web.dto.EventsListResponseDto;
import com.snowroad.event.domain.Events;
import com.snowroad.event.domain.EventsRepository;
import com.snowroad.event.web.dto.EventsResponseDto;
import com.snowroad.event.web.dto.EventsSaveRequestDto;
import com.snowroad.event.web.dto.PagedResponseDto;
import com.snowroad.file.web.dto.EventsFileDetailResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public List<AdminEventsListResponseDto> findAllDesc() {
        return eventsRepository.findAllDesc().stream()
                .map(AdminEventsListResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public void delete(Long id) {
        Events events = eventsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 없습니다. id="+id));
        eventsRepository.delete(events);
    }

    // 이벤트 상세 이미지 조회
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

    // 썸네일 이미지 정보 조회
    public List<EventsFileDetailResponseDTO> getTumbFilesDtlList(Long eventId) {
        // Native Query 호출
        List<Object[]> result =  eventsRepository.findTumbFilesDtlByEventId(eventId);
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

    @Transactional(readOnly = true)
    public List<EventsListResponseDto> findEvntList() {
        return eventsRepository.findEvntList().stream()
                .map(EventsListResponseDto::new)
                .collect(Collectors.toList());

    }

    public EventsResponseDto findEvntData(Long id) {
        Events entity = eventsRepository.findById(id)
                .orElseThrow(() -> new
                        IllegalArgumentException("해당 팝업/전시가 존재하지 않습니다. id" + id));
        return new EventsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<EventsListResponseDto> findEvntMarkedList() {
        return eventsRepository.findEvntMarkedList().stream()
                .map(EventsListResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<EventsListResponseDto> getMainRankList() {
        return eventsRepository.getMainRankList().stream()
                .map(EventsListResponseDto::new)
                .collect(Collectors.toList());

    }
    @Transactional(readOnly = true)
    public List<EventsListResponseDto> getMainOperStatList() {
        return eventsRepository.getMainOperStatList().stream()
                .map(EventsListResponseDto::new)
                .collect(Collectors.toList());

    }
    @Transactional(readOnly = true)
    public List<EventsListResponseDto> getMainOperEndList() {
        return eventsRepository.getMainOperEndList().stream()
                .map(EventsListResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public PagedResponseDto<Events> getEventByPagination(int page) {
        int size = 20;
        Sort sort = Sort.by(Sort.Direction.DESC, "eventId");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Events> eventsPage = eventsRepository.findAll(pageable);

        return new PagedResponseDto<>(
                eventsPage.getContent(),
                eventsPage.getTotalPages()
        );

    }
}
