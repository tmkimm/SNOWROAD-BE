package com.snowroad.event.domain;

import com.snowroad.config.auth.dto.CustomUserDetails;
import com.snowroad.event.web.dto.DetailEventsResponseDto;
import com.snowroad.event.web.dto.EventContentsResponseDto;
import com.snowroad.event.web.dto.HomeEventsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventsRepositoryCustom{


    // 쿼리dsl 테스트
    List<HomeEventsResponseDto> getMainTestList(String eventTypeCd);
    List<HomeEventsResponseDto> getMainRcmnList(String eventTypeCd, CustomUserDetails userDetails);

    Page<DetailEventsResponseDto> getEvntList(Pageable page, String eventTypeCd, String sortType, List<String> ctgyId, String fromDate, String toDate, List<String> geo, Long userId);

    // 상세 페이지
    EventContentsResponseDto findEvntData(Long eventId);

}
