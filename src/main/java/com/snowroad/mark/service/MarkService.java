package com.snowroad.mark.service;

import com.snowroad.mark.domain.Mark;
import com.snowroad.mark.domain.MarkRepository;
import com.snowroad.mark.web.dto.MarkSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MarkService {
    private final MarkRepository markRepository;

    @Transactional
    public Long save(MarkSaveRequestDto requestDto) {
        return markRepository.save(requestDto.toEntity()).getUserAcntNo();
    }

    @Transactional
    public Long update(Long id, String evntId, MarkSaveRequestDto requestDto) {
        Mark events = markRepository.findById(id)
                .orElseThrow(() -> new
        IllegalArgumentException("해당 팝업/전시는 더이상 좋아요를 추가할 수 없어요. 팝업ID :: " + evntId));

        events.update(requestDto.getUserAcntNo(), requestDto.getEventId(), requestDto.getLikeYn());
        return id;
//      return markRepository.save(requestDto.toEntity()).getUserAcntNo(); // 실제 db 작업시 위 주석으로 변경
    }

    public void findById(Long id, String evntId) {
//        Events entity = markRepository.findById(id)
//                .orElseThrow(() -> new
//                        IllegalArgumentException("이벤트가 존재하지 않습니다. id" + id));
//        return new EventsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public void findAllDesc() {
//        markRepository.findAllDesc().stream()
//                .map(EventsListResponseDto::new)
//                .collect(Collectors.toList());

    }

/*    @Transactional
    public void delete(Long id) {
        Mark events = markRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 즐겨찾기가 없습니다. id="+id));
        markRepository.delete(events);
    }*/
}
