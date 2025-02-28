package com.snowroad.event.domain;

import com.snowroad.event.web.dto.HomeEventsResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventsRepositoryCustom{


    // 쿼리dsl 테스트
    List<HomeEventsResponseDto> getMainTestList(String eventTypeCd);



}
