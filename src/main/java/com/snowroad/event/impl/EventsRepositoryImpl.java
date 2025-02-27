package com.snowroad.event.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.event.domain.EventsRepositoryCustom;
import com.snowroad.entity.QEvents;
import com.snowroad.event.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventsRepositoryImpl implements EventsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<HomeEventsResponseDto> getMainTestList(String eventTypeCd) {
        QEvents e = QEvents.events;

        List<Tuple> result = queryFactory
                .select(
                        e.eventId,
                        e.eventNm,
                        e.operStatDt.substring(2, 8),
                        e.operEndDt.substring(2, 8),
                        e.ctgyId,
                        e.eventTypeCd
                )
                .from(e)
//                .where(
//                        e.operEndDt.goe(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))),
//                        eventTypeCd.equals("ALL") ? null : e.eventTypeCd.eq(eventTypeCd)
//                )
                .limit(10)
                .fetch();
        System.out.println(result);

        return result.stream().map(row -> HomeEventsResponseDto.builder()
                .eventId(row.get(e.eventId))
                .eventNm(row.get(e.eventNm))
                .operStatDt(row.get(e.operStatDt.substring(2, 8)))
                .operEndDt(row.get(e.operEndDt.substring(2, 8)))
                .ctgyId(row.get(e.ctgyId))
                .eventTypeCd(row.get(e.eventTypeCd))
                .build()
        ).collect(Collectors.toList());
    }
}

