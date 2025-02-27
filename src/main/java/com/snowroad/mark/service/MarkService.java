package com.snowroad.mark.service;

import com.snowroad.mark.domain.MarkRepository;
import com.snowroad.mark.web.dto.MarkedEventResponseDTO;
import com.snowroad.mark.web.dto.MarkSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MarkService {
    private final MarkRepository markRepository;

    @Transactional
    public Long saveMarkEvent(MarkSaveRequestDto requestDto) {
        return markRepository.saveMarkEvent(requestDto);
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

    @Transactional(readOnly = true)
    public List<MarkedEventResponseDTO> getMarkedEventList(Long userId) {

        // Native Query 호출
        List<MarkedEventResponseDTO> result =  markRepository.getMarkedEventList(userId);

        return result;

    }

    @Transactional(readOnly = true)
    public void findAllDesc() {
//        markRepository.findAllDesc().stream()
//                .map(EventsListResponseDto::new)
//                .collect(Collectors.toList());

    }

}
