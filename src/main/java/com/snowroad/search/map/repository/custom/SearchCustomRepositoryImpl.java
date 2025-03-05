package com.snowroad.search.map.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.entity.Events;
import com.snowroad.entity.QEvents;
import com.snowroad.search.map.dto.SearchRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * Map 검색 커스텀 레포 구현체
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-02-28
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

        //이벤트 텍스트 검색 데이터 - IN을 통한 이벤트 ID 전제 검색
        if (requestDTO.getKeyword() != null) {
            builder.and(qEvents.eventId.in(requestDTO.getEventIds()));
        }

        //지도 거리 계산
        if(requestDTO.getLatitude() != null && requestDTO.getLongitude() != null){
            BooleanExpression latCondition = qEvents.addrLttd.between(requestDTO.getMinLat(), requestDTO.getMaxLat());
            BooleanExpression lonCondition = qEvents.addrLotd.between(requestDTO.getMinLon(), requestDTO.getMaxLon());
            BooleanTemplate locationGroupedCondition = Expressions.booleanTemplate("(({0}) AND ({1}))", latCondition, lonCondition);
            builder.and(locationGroupedCondition);
        }

        //시작일자, 종료일자
        if (requestDTO.getOperStatDt() != null && requestDTO.getOperEndDt() != null) {
            BooleanExpression statCondition = qEvents.operStatDt.loe(requestDTO.getOperEndDt());
            BooleanExpression endCondition = qEvents.operEndDt.goe(requestDTO.getOperStatDt());
            BooleanTemplate dateGroupedCondition = Expressions.booleanTemplate("(({0}) AND ({1}))", statCondition, endCondition);
            builder.and(dateGroupedCondition);
        }

        //이벤트 구분 코드
        if (requestDTO.getEventTypeCd() != null) {
            builder.and(qEvents.eventTypeCd.eq(requestDTO.getEventTypeCd()));
        }

        return queryFactory
            .selectFrom(qEvents)
            .where(builder)
            .fetch();
    }

}
