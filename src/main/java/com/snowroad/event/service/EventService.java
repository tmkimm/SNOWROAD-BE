package com.snowroad.event.service;

import com.snowroad.admin.web.dto.AdminEventsListResponseDto;
import com.snowroad.event.web.dto.*;
import com.snowroad.event.domain.Events;
import com.snowroad.event.domain.EventsRepository;
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
    public List<DetailEventsResponseDto> getEvntList(String sortType) {

        // Native Query 호출
        List<Object[]> result =  eventsRepository.getEvntList(sortType);
        // Object[]에서 데이터를 추출하여 필요한 형태로 가공
        List<DetailEventsResponseDto> eventDtlListData = result.stream().map(row -> {
                    DetailEventsResponseDto evntList = new DetailEventsResponseDto();
                    evntList.setEventId((Long) row[0]);
                    evntList.setEventNm((String) row[1]);
                    evntList.setEventCntn((String) row[2]);
                    evntList.setEventAddr((String) row[3]);
                    evntList.setOperStatDt((String) row[4]);
                    evntList.setOperEndDt((String) row[5]);
                    evntList.setCtgyId((String) row[6]);
                    //    evntList.setCtgyNm((String) row[7]);
                    evntList.setEventTypeCd((String) row[7]);
                    //    evntList.setEventTypeNm((String) row[9]);
                    evntList.setTumbFileId((Long) row[8]);
                    evntList.setViewNmvl((Long) row[9]);
                    evntList.setLikeYn((Character) row[10]);
                    return evntList;
                })
                .collect(Collectors.toList());

        return eventDtlListData;

    }

    public EventsResponseDto findEvntData(Long id) {
        Events entity = eventsRepository.findById(id)
                .orElseThrow(() -> new
                        IllegalArgumentException("해당 팝업/전시가 존재하지 않습니다. id" + id));
        return new EventsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<DetailEventsResponseDto> findEvntMarkedList() {
        // Native Query 호출
        List<Object[]> result =  eventsRepository.findEvntMarkedList();
        // Object[]에서 데이터를 추출하여 필요한 형태로 가공
        List<DetailEventsResponseDto> eventMarkedData = result.stream().map(row -> {
                    DetailEventsResponseDto evntMarkList = new DetailEventsResponseDto();
                    evntMarkList.setEventId((Long) row[0]);
                    evntMarkList.setEventNm((String) row[1]);
                    evntMarkList.setEventCntn((String) row[2]);
                    evntMarkList.setEventAddr((String) row[3]);
                    evntMarkList.setOperStatDt((String) row[4]);
                    evntMarkList.setOperEndDt((String) row[5]);
                    evntMarkList.setCtgyId((String) row[6]);
                    //    evntRankList.setCtgyNm((String) row[7]);
                    evntMarkList.setEventTypeCd((String) row[7]);
                    //    evntRankList.setEventTypeNm((String) row[9]);
                    evntMarkList.setTumbFileId((Long) row[8]);
                    evntMarkList.setViewNmvl((Long) row[9]);
                    evntMarkList.setLikeYn((Character) row[10]);
                    return evntMarkList;
                })
                .collect(Collectors.toList());

        return eventMarkedData;

    }

    @Transactional(readOnly = true)
    public List<HomeEventsResponseDto> getMainRankList(String eventTypeCd) {
        // Native Query 호출
        List<Object[]> result =  eventsRepository.getMainRankList(eventTypeCd);
        // Object[]에서 데이터를 추출하여 필요한 형태로 가공
        List<HomeEventsResponseDto> eventRankListData = result.stream().map(row -> {
                    HomeEventsResponseDto evntRankList = new HomeEventsResponseDto();
                    evntRankList.setEventId((Long) row[0]);
                    evntRankList.setEventNm((String) row[1]);
                    evntRankList.setEventCntn((String) row[2]);
                    evntRankList.setEventAddr((String) row[3]);
                    evntRankList.setOperStatDt((String) row[4]);
                    evntRankList.setOperEndDt((String) row[5]);
                    evntRankList.setCtgyId((String) row[6]);
                //    evntRankList.setCtgyNm((String) row[7]);
                    evntRankList.setEventTypeCd((String) row[7]);
                //    evntRankList.setEventTypeNm((String) row[9]);
                    evntRankList.setTumbFileId((Long) row[8]);
                    evntRankList.setViewNmvl((Long) row[9]);
                    evntRankList.setLikeYn((String) row[10]);
                    return evntRankList;
                })
                .collect(Collectors.toList());

        return eventRankListData;

    }
    @Transactional(readOnly = true)
    public List<HomeEventsResponseDto> getMainOperStatList(String eventTypeCd) {
/*        return eventsRepository.getMainOperStatList().stream()
                .map(EventsListResponseDto::new)
                .collect(Collectors.toList());*/
        // Native Query 호출
        List<Object[]> result =  eventsRepository.getMainOperStatList(eventTypeCd);
        // Object[]에서 데이터를 추출하여 필요한 형태로 가공
        List<HomeEventsResponseDto> eventOperDateListData = result.stream().map(row -> {
                    HomeEventsResponseDto evntOperDateList = new HomeEventsResponseDto();
                    evntOperDateList.setEventId((Long) row[0]);
                    evntOperDateList.setEventNm((String) row[1]);
                    evntOperDateList.setEventCntn((String) row[2]);
                    evntOperDateList.setEventAddr((String) row[3]);
                    evntOperDateList.setOperStatDt((String) row[4]);
                    evntOperDateList.setOperEndDt((String) row[5]);
                    evntOperDateList.setCtgyId((String) row[6]);
                    //    evntRankList.setCtgyNm((String) row[7]);
                    evntOperDateList.setEventTypeCd((String) row[7]);
                    //    evntRankList.setEventTypeNm((String) row[9]);
                    evntOperDateList.setTumbFileId((Long) row[8]);
                    evntOperDateList.setViewNmvl((Long) row[9]);
                    evntOperDateList.setLikeYn((String) row[10]);
                    return evntOperDateList;
                })
                .collect(Collectors.toList());

        return eventOperDateListData;
    }
    @Transactional(readOnly = true)
    public List<HomeEventsResponseDto> getMainOperEndList(String eventTypeCd) {
/*        return eventsRepository.getMainOperEndList().stream()
                .map(EventsListResponseDto::new)
                .collect(Collectors.toList());*/
        // Native Query 호출
        List<Object[]> result =  eventsRepository.getMainOperEndList(eventTypeCd);
        // Object[]에서 데이터를 추출하여 필요한 형태로 가공
        List<HomeEventsResponseDto> eventOperDateListData = result.stream().map(row -> {
                    HomeEventsResponseDto evntOperDateList = new HomeEventsResponseDto();
                    evntOperDateList.setEventId((Long) row[0]);
                    evntOperDateList.setEventNm((String) row[1]);
                    evntOperDateList.setEventCntn((String) row[2]);
                    evntOperDateList.setEventAddr((String) row[3]);
                    evntOperDateList.setOperStatDt((String) row[4]);
                    evntOperDateList.setOperEndDt((String) row[5]);
                    evntOperDateList.setCtgyId((String) row[6]);
                    //    evntRankList.setCtgyNm((String) row[7]);
                    evntOperDateList.setEventTypeCd((String) row[7]);
                    //    evntRankList.setEventTypeNm((String) row[9]);
                    evntOperDateList.setTumbFileId((Long) row[8]);
                    evntOperDateList.setViewNmvl((Long) row[9]);
                    evntOperDateList.setLikeYn((String) row[10]);
                    return evntOperDateList;
                })
                .collect(Collectors.toList());
        return eventOperDateListData;
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
