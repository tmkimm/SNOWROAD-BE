package com.snowroad.mark.domain;

import com.snowroad.event.web.dto.EventsSaveRequestDto;
import com.snowroad.mark.web.dto.MarkSaveRequestDto;
import com.snowroad.mark.web.dto.MarkedEventResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarkRepository {

    // 쿼리dsl 테스트
    Page<MarkedEventResponseDTO> getMarkedEventList(Pageable page, Long userId);

    Long addMarkEvent(MarkSaveRequestDto requestDto);

}
