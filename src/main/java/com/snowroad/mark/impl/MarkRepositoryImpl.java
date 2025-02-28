package com.snowroad.mark.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.entity.QEvents;
import com.snowroad.entity.Mark;
import com.snowroad.mark.domain.MarkRepository;
import com.snowroad.entity.QMark;
import com.snowroad.mark.web.dto.MarkSaveRequestDto;
import com.snowroad.mark.web.dto.MarkedEventResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MarkRepositoryImpl implements MarkRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MarkedEventResponseDTO> getMarkedEventList(Long userId) {
        QEvents e = QEvents.events;
        QMark m = new QMark("m");

        List<Tuple> result = queryFactory
                .select(
                        e.eventId,
                        e.eventNm,
                        e.eventCntn,
                        e.eventAddr,
                        e.operStatDt.substring(2, 8),
                        e.operEndDt.substring(2, 8),
                        e.ctgyId,
                        e.eventTypeCd,
                        m.likeYn
                )
                .from(e)
                .join(m).on(e.eventId.eq(m.eventId))
                .where(
                        m.userAcntNo.eq(userId),
                        m.likeYn.eq("Y")
//                        eventTypeCd.equals("ALL") ? null : e.eventTypeCd.eq(eventTypeCd)
                )
                .limit(10)
                .fetch();

        return result.stream().map(row -> MarkedEventResponseDTO.builder()
                .eventId(row.get(e.eventId))
                .eventNm(row.get(e.eventNm))
                .eventCntn(row.get(e.eventCntn))
                .eventAddr(row.get(e.eventAddr))
                .operStatDt(row.get(e.operStatDt.substring(2, 8)))
                .operEndDt(row.get(e.operEndDt.substring(2, 8)))
                .ctgyId(row.get(e.ctgyId))
                .eventTypeCd(row.get(e.eventTypeCd))
                .likeYn(row.get(m.likeYn))
                .imageUrl(null)
                .smallImageUrl(null)
                .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Long saveMarkEvent(MarkSaveRequestDto requestDto){

        // 1) requestDto에서 id를 가져와, 기존에 즐겨찾기를 추가한 항목인지 체크




        // 2) 즐겨찾기 데이터가 없을시 : 신규 등록



        // 3) 즐겨찾기 데이터가 있을시 : like_yn 수정
        Mark mark = Mark.builder().build();
        return mark.getEventId();

    }

}

