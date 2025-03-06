package com.snowroad.event.domain;

import com.snowroad.event.web.dto.DetailEventsResponseDto;
import com.snowroad.event.web.dto.HomeEventsResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventsRepositoryCustom{


    // 쿼리dsl 테스트
    List<HomeEventsResponseDto> getMainTestList(String eventTypeCd);

    List<DetailEventsResponseDto> getEvntList(String eventTypeCd, String sortType, List<String> ctgyId, String fromDate, String toDate, List<String> geo);
}
