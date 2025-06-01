package com.snowroad.event.service;

import com.snowroad.MorphemeAnalyzer.annotation.MorphIndexing;
import com.snowroad.admin.web.dto.AdminEventsListResponseDto;
import com.snowroad.common.exception.EventNotFoundException;
import com.snowroad.config.auth.dto.CustomUserDetails;
import com.snowroad.event.domain.EventsRepositoryCustom;
import com.snowroad.event.web.dto.*;
import com.snowroad.entity.Events;
import com.snowroad.event.domain.EventsRepository;
import com.snowroad.file.web.dto.EventsFileDetailResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventsRepository eventsRepository;

    @Qualifier("eventsRepositoryImpl")  // 명확하게 지정하여 Spring이 올바른 빈을 주입하도록 함
    private final EventsRepositoryCustom eventsRepositoryCustom;

    @MorphIndexing
    @Transactional
    public Long save(EventsSaveRequestDto requestDto) {
        return eventsRepository.save(requestDto.toEntity()).getEventId();
    }

    @Transactional
    public Long update(Long id, EventsSaveRequestDto requestDto) {
        Events events = eventsRepository.findById(id)
                .orElseThrow(() -> new
        IllegalArgumentException("이벤트가 존재하지 않습니다. id" + id));

        events.update(requestDto.getEventNm(), requestDto.getEventCntn(), requestDto.getEventAddr(), requestDto.getOperStatDt(), requestDto.getOperEndDt(), requestDto.getOperDttmCntn(), requestDto.getCtgyId(), requestDto.getPpstEnbnTypeCd(), requestDto.getAddrLttd(), requestDto.getAddrLotd(), requestDto.getLdcd(), requestDto.getRads(), requestDto.getLnad(), requestDto.getEventDetailUrl());
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
    public PagedResponseDto<DetailEventsResponseDto> getEvntList(int page, String eventTypeCd, String sortType, List<String> ctgyId, String fromDate, String toDate, List<String> geo, Long userId) {

        int size = 12; // 임의로 한페이지에 12개 데이터 처리, 추후 필요시 변경
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operStatDt"));

        Page<DetailEventsResponseDto> eventsPage = eventsRepositoryCustom.getEvntList(pageable,eventTypeCd, sortType,ctgyId,fromDate,toDate,geo,userId);

        return new PagedResponseDto<>(
                eventsPage.getContent(),
                eventsPage.getTotalPages()
        );

    }


    @Transactional(readOnly = true)
    public List<HomeEventsResponseDto> getNearEvntList(Long eventId) {
        return eventsRepositoryCustom.getNearEvntList(eventId);
    }


    public EventDetailWithNearEvents findEvntData(Long eventId, Long userId) {
        EventContentsResponseDto eventDetails = eventsRepositoryCustom.findEvntData(eventId, userId);
        // 결과가 null인 경우 예외 발생
        if (eventDetails == null) {
            throw new EventNotFoundException("해당하는 컨텐츠가 존재하지 않습니다.");
            // 또는 JPA를 사용한다면 javax.persistence.EntityNotFoundException 등을 사용할 수도 있습니다.
        }
        List<HomeEventsResponseDto> nearEvents = eventsRepositoryCustom.getNearEvntList(eventId);
        return new EventDetailWithNearEvents(eventDetails, nearEvents);
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
                    //  evntMarkList.setTumbFileId((Long) row[8]);
                    // evntMarkList.setViewNmvl((Long) row[9]);
                    evntMarkList.setLikeYn((String) row[8]);
                    return evntMarkList;
                })
                .collect(Collectors.toList());

        return eventMarkedData;

    }

    @Transactional(readOnly = true)
    public List<HomeEventsResponseDto> getMainBannerList(String eventTypeCd) {
        // Native Query 호출
        List<Object[]> result =  eventsRepository.getMainBannerList(eventTypeCd);
        // Object[]에서 데이터를 추출하여 필요한 형태로 가공
        List<HomeEventsResponseDto> eventBannerListData = result.stream().map(row -> {
                    HomeEventsResponseDto evntBannerList = new HomeEventsResponseDto();
                    evntBannerList.setEventId((Long) row[0]);
                    evntBannerList.setEventNm((String) row[1]);
                    evntBannerList.setOperStatDt((String) row[2]);
                    evntBannerList.setOperEndDt((String) row[3]);
                    evntBannerList.setCtgyId((String) row[4]);
                    evntBannerList.setEventTypeCd((String) row[5]);
                    evntBannerList.setImageUrl((String) row[6]);
                    evntBannerList.setSmallImageUrl((String) row[7]);
                    return evntBannerList;
                })
                .collect(Collectors.toList());

        return eventBannerListData;
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
                    evntRankList.setOperStatDt((String) row[2]);
                    evntRankList.setOperEndDt((String) row[3]);
                    evntRankList.setCtgyId((String) row[4]);
                    evntRankList.setEventTypeCd((String) row[5]);
                    evntRankList.setImageUrl((String) row[6]);
                    evntRankList.setSmallImageUrl((String) row[7]);
                    return evntRankList;
                })
                .collect(Collectors.toList());

        return eventRankListData;
    }

    @Transactional(readOnly = true)
    public Map<String, List<EventsGeoFilterDto>> getEventGeoFilter() {
        // Native Query 호출
        List<Object[]> result =  eventsRepository.getEventGeoFilter(); // 지역그룹단위구분코드

        // Object[]에서 데이터를 추출하여 필요한 형태로 가공
        Map<String, List<EventsGeoFilterDto>> eventGeoFilterData = result.stream().map(row -> {
                    EventsGeoFilterDto evntGeoList = new EventsGeoFilterDto();
                    evntGeoList.setRgntCd((String) row[0]);
                    evntGeoList.setRegionName((String) row[1]);
                    evntGeoList.setRgntTypeCd((String) row[2]);
                    evntGeoList.setCnt((Long) row[3]);
                    evntGeoList.setLcdcNm((String) row[4]);
                    return evntGeoList;
                })
                .collect(Collectors.groupingBy(EventsGeoFilterDto::getRgntTypeCd));


        return eventGeoFilterData;

    }

@Transactional(readOnly = true)
public Map<String, Object> getMainRcmnList(String eventTypeCd, CustomUserDetails userDetails) {
    // Querydsl 구현체 호출
    List<HomeEventsResponseDto> result =  eventsRepositoryCustom.getMainRcmnList(eventTypeCd, userDetails);
    // ctgyId별로 리스트를 묶어서 Map으로 변환
    Map<String,List<HomeEventsResponseDto>> eventsByCategory = result.stream()
            .collect(Collectors.groupingBy(HomeEventsResponseDto::getCtgyId));

    // 전체 리스트와 카테고리별 리스트를 담을 Map 생성
    Map<String, Object> resultData = new HashMap<>();
    resultData.put("all", result);
    resultData.put("categories", eventsByCategory);

    return resultData;
//    return result;
}

    @Transactional(readOnly = true)
    public List<HomeEventsResponseDto> getMainOperStatList(String eventTypeCd) {
        // Native Query 호출
        List<Object[]> result =  eventsRepository.getMainOperStatList(eventTypeCd);
        // Object[]에서 데이터를 추출하여 필요한 형태로 가공
        List<HomeEventsResponseDto> eventOperDateListData = result.stream().map(row -> {
                    HomeEventsResponseDto evntOperDateList = new HomeEventsResponseDto();
                    evntOperDateList.setEventId((Long) row[0]);
                    evntOperDateList.setEventNm((String) row[1]);
                    evntOperDateList.setOperStatDt((String) row[2]);
                    evntOperDateList.setOperEndDt((String) row[3]);
                    evntOperDateList.setCtgyId((String) row[4]);
                    evntOperDateList.setEventTypeCd((String) row[5]);
                    evntOperDateList.setImageUrl((String) row[6]);
                    evntOperDateList.setSmallImageUrl((String) row[7]);
                    evntOperDateList.setDDay((String) row[8]);
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
                    evntOperDateList.setOperStatDt((String) row[2]);
                    evntOperDateList.setOperEndDt((String) row[3]);
                    evntOperDateList.setCtgyId((String) row[4]);
                    evntOperDateList.setEventTypeCd((String) row[5]);
                    evntOperDateList.setImageUrl((String) row[6]);
                    evntOperDateList.setSmallImageUrl((String) row[7]);
                    evntOperDateList.setDDay((String) row[8]);
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
