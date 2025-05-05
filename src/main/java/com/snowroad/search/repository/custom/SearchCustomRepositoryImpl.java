package com.snowroad.search.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.entity.Events;
import com.snowroad.entity.QEvents;
import com.snowroad.search.dto.SearchRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * 검색 & 리스트 조회
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-05-05
 *
 */
@Repository
@RequiredArgsConstructor
public class SearchCustomRepositoryImpl implements SearchCustomRepository {

    private final JPAQueryFactory queryFactory;

    /**
     *
     * 이벤트 데이터 검색을 수행한다.
     *
     * @author hyo298, 김재효
     * @param pageable 페이징 설정
     * @param requestDTO 검색 조건
     * @return List<Long>
     */
    @Override
    public Page<Events> findSearchEventDataList(Pageable pageable, SearchRequestDTO requestDTO) {
        QEvents qEvents = QEvents.events;
        BooleanBuilder builder = setSearchCondition(qEvents, requestDTO);

        // step.4 이벤트 조회
        List<Events> events = queryFactory
                .selectFrom(qEvents)
                .where(builder)
                .orderBy(getSearchOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // step.5 이벤트 전체 건수
        long totalCount = Optional.ofNullable(
                queryFactory
                .select(qEvents.count())
                .from(qEvents)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(events, pageable, totalCount);
    }

    /**
     *
     * 이벤트 데이터 검색 조건을 생성한다.
     *
     * @author hyo298, 김재효
     * @param qEvents 페이징 설정
     * @param requestDTO 검색 조건
     * @return List<Long>
     */
    private BooleanBuilder setSearchCondition(QEvents qEvents, SearchRequestDTO requestDTO) {
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

        // step.3 이벤트 구분 코드
        if (requestDTO.getEventTypeCd() != null) {
            builder.and(qEvents.eventTypeCd.eq(requestDTO.getEventTypeCd()));
        }

        // step.4 이벤트 삭제 여부
        builder.and(qEvents.deleteYn.eq("N"));

        return builder;
    }

    /**
     *
     * 이벤트 데이터 검색 정렬 조건을 구성한다.
     *
     * @author hyo298, 김재효
     * @param sort 페이징 정렬 조건
     * @return List<Long>
     */

    private OrderSpecifier<?>[] getSearchOrderSpecifiers(Sort sort) {
        PathBuilder<Events> entityPath = new PathBuilder<>(Events.class, "events");
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        // 클라이언트가 요청한 정렬 순서 반영
        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            orderSpecifiers.add(
                    new OrderSpecifier<>(
                            direction,
                            // Comparable 타입으로 명시
                            entityPath.get(order.getProperty(), Comparable.class)
                    )
            );
        }

        // 보조 정렬: operStatDt 값이 동일할 때 일관된 순서를 위해 id 역순 추가
        orderSpecifiers.add(
                new OrderSpecifier<>(
                        Order.DESC,
                        entityPath.get("eventId", Long.class)
                )
        );

        return orderSpecifiers.toArray(new OrderSpecifier<?>[0]);
    }
}
