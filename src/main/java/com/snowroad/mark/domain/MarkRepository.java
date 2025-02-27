package com.snowroad.mark.domain;

import com.snowroad.event.web.dto.EventsSaveRequestDto;
import com.snowroad.mark.web.dto.MarkSaveRequestDto;
import com.snowroad.mark.web.dto.MarkedEventResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarkRepository {

    // 쿼리dsl 테스트
    List<MarkedEventResponseDTO> getMarkedEventList(Long userId);

    Long saveMarkEvent(MarkSaveRequestDto requestDto);

}
