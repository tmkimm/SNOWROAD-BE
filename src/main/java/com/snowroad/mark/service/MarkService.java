package com.snowroad.mark.service;

import com.snowroad.event.web.dto.PagedResponseDto;
import com.snowroad.mark.domain.MarkRepository;
import com.snowroad.mark.web.dto.MarkedEventResponseDTO;
import com.snowroad.mark.web.dto.MarkSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MarkService {
    private final MarkRepository markRepository;

    @Transactional(readOnly = true)
    public PagedResponseDto<MarkedEventResponseDTO> getMarkedEventList(int page, Long userId) {
        int size = 12; // 임의로 한페이지에 20개 데이터 처리, 추후 필요시 변경
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operStatDt"));
        Page<MarkedEventResponseDTO> eventsPage = markRepository.getMarkedEventList(pageable,userId);

        return new PagedResponseDto<>(
                eventsPage.getContent(),
                eventsPage.getTotalPages()
        );
    }

    @Transactional
    public Long addMarkEvent(MarkSaveRequestDto requestDto) {
        return markRepository.addMarkEvent(requestDto);
    }

    @Transactional
    public Long update(Long id, Long evntId, MarkSaveRequestDto requestDto) {
//        Mark events = markRepository.findById(id)
//                .orElseThrow(() -> new
//        IllegalArgumentException("해당 팝업/전시는 더이상 좋아요를 추가할 수 없어요. 팝업ID :: " + evntId));
//
//        events.update(requestDto.getUserAcntNo(), requestDto.getEventId(), requestDto.getLikeYn());
        return id;
//      return markRepository.save(requestDto.toEntity()).getUserAcntNo(); // 실제 db 작업시 위 주석으로 변경
    }



}
