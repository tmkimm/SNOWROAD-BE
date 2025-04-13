package com.snowroad.geodata.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.entity.Events;
import com.snowroad.entity.QEvents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * 거리표준검색 커스텀 레포지포리 구현체
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-04-07
 *
 */
@Repository
@RequiredArgsConstructor
public class GeoDataCustomRepositoryImpl implements GeoDataCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Events> findGeoMapDataList(double minLat, double maxLat, double minLon, double maxLon) {
        QEvents qEvents = QEvents.events;
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression latCondition = qEvents.addrLttd.between(minLat, maxLat);
        BooleanExpression lonCondition = qEvents.addrLotd.between(minLon, maxLon);
        BooleanTemplate locationGroupedCondition = Expressions.booleanTemplate("(({0}) AND ({1}))", latCondition, lonCondition);
        builder.and(locationGroupedCondition);

        return queryFactory
            .selectFrom(qEvents)
            .where(builder)
            .fetch();
    }

}
