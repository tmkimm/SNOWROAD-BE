package com.snowroad.search.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.entity.Events;
import com.snowroad.entity.QEvents;
import com.snowroad.search.dto.SearchRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * 검색 커스텀 레포지토리 구현체
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-03-05
 *
 */
@Repository
@RequiredArgsConstructor
public class SearchCustomRepositoryImpl implements SearchCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Events> findLocationMapDataList(SearchRequestDTO requestDTO) {
        QEvents qEvents = QEvents.events;
        BooleanBuilder builder = new BooleanBuilder();

        // step.1 사전 데이터 in 구성(Text 검색, 거리표준)
        if (requestDTO.getKeyword() != null) {
            builder.and(qEvents.eventId.in(requestDTO.getEventIds()));
        }

        // step.2 시작일자, 종료일자
        if (requestDTO.getOperStatDt() != null && requestDTO.getOperEndDt() != null) {
            BooleanExpression statCondition = qEvents.operStatDt.loe(requestDTO.getOperEndDt());
            BooleanExpression endCondition = qEvents.operEndDt.goe(requestDTO.getOperStatDt());
            BooleanTemplate dateGroupedCondition = Expressions.booleanTemplate("(({0}) AND ({1}))", statCondition, endCondition);
            builder.and(dateGroupedCondition);
        }

        // step.2 이벤트 구분 코드
        if (requestDTO.getEventTypeCd() != null) {
            builder.and(qEvents.eventTypeCd.eq(requestDTO.getEventTypeCd()));
        }

        return queryFactory
            .selectFrom(qEvents)
            .where(builder)
            .fetch();
    }

}
