package com.snowroad.search.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.entity.Events;
import com.snowroad.entity.QEvents;
import com.snowroad.geodata.util.HaversineFormula;
import com.snowroad.search.dto.QSearchResponseDTO;
import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.dto.SearchResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
     * 이벤트 데이터 검색을 수행한다.
     *
     * @param requestDTO 검색 조건
     * @return List<Long>
     * @author hyo298, 김재효
     */
    @Override
    public Page<SearchResponseDTO> findSearchEventDataList(SearchRequestDTO requestDTO) {
        // step.1 이벤트 조건 설정
        QEvents qEvents = QEvents.events;
        BooleanBuilder builder = setSearchCondition(qEvents, requestDTO);

        // step.2 공통 쿼리 구성
        JPAQuery<SearchResponseDTO> searchQuery = queryFactory
                .select(new QSearchResponseDTO(
                        qEvents.eventId,
                        qEvents.eventNm,
                        qEvents.eventCntn,
                        qEvents.eventAddr,
                        qEvents.rads,
                        qEvents.lnad,
                        qEvents.operStatDt,
                        qEvents.operEndDt,
                        qEvents.operDttmCntn,
                        qEvents.ctgyId,
                        qEvents.eventTypeCd,
                        qEvents.addrLttd,
                        qEvents.addrLotd,
                        qEvents.ldcd,
                        qEvents.eventTumbfile.fileMstId,
                        qEvents.eventFiles.fileMstId,
                        qEvents.eventView.viewNmvl
                ))
                .from(qEvents)
                .where(builder);

        // step.3 정렬 설정
        String sortType;
        if (requestDTO.hasSortType()) {
            sortType = requestDTO.getSortType();
        } else {
            sortType = "최신순";
        }
        
        Sort sort = switch (sortType) {
            case "인기순" -> Sort.by(Sort.Direction.DESC, "eventView.viewNmvl");
            case "마감순" -> Sort.by(Sort.Direction.DESC, "operEndDt");
            default -> Sort.by(Sort.Direction.DESC, "operStatDt");
        };
        Pageable pageable = PageRequest.of(requestDTO.getPage(), 12, sort);

        List<SearchResponseDTO> events = searchQuery.fetch();
        Stream<SearchResponseDTO> eventStream = events.stream();

        // step.3-1 거리순 정렬 설정
        boolean isDistanceSort = "거리순".equalsIgnoreCase(sortType);
        if (!isDistanceSort) {
            processDefaultSort(searchQuery, pageable);
        }

        // step.4 거리 값 처리
        if (requestDTO.hasCoordinate()) {
            double latitude = requestDTO.getLatitude();
            double longitude = requestDTO.getLongitude();
            eventStream = eventStream
                .filter(dto -> dto.getAddrLttd() != null && dto.getAddrLotd() != null)
                .peek(dto -> {
                    double dist = HaversineFormula.calculateDistance(latitude, longitude, dto.getAddrLttd(), dto.getAddrLotd());
                    dto.setDistanceKm(dist);

                    // step.4-1 사용자 표시 거리 표기
                    String displayDistance;
                    if (dist < 1.0) {
                        displayDistance = Math.round(dist * 1000) + "m";
                    } else {
                        displayDistance = Math.round(dist * 10) / 10.0 + "km";
                    }
                    dto.setDisplayDistance(displayDistance);
                });
        }

        // step.3-2, 4-1 거리순 정렬 조건인 경우 필터 적용
        // 3-1과 연계 로직
        if (isDistanceSort) {
            eventStream = eventStream
                // 기본 거리 표준 필터 해제
                // .filter(dto -> dto.getDistance() <= SearchMapEnum.MAP_DISTANCE_STANDARD.getRate())
                .sorted(Comparator.comparingDouble(SearchResponseDTO::getDistanceKm).reversed());
        }

        // step.5 결과
        List<SearchResponseDTO> sortedList = eventStream.toList();
        if (isDistanceSort) {
            int start = Math.min((int) pageable.getOffset(), sortedList.size());
            int end = Math.min(start + pageable.getPageSize(), sortedList.size());
            List<SearchResponseDTO> pagedList = sortedList.subList(start, end);
            return new PageImpl<>(pagedList, pageable, sortedList.size());
        } else {
            long totalCount = Optional.ofNullable(
                    queryFactory
                            .select(qEvents.count())
                            .from(qEvents)
                            .where(builder)
                            .fetchOne()
            ).orElse(0L);
            return new PageImpl<>(sortedList, pageable, totalCount);
        }
    }

    /**
     *
     * 거리순 Stream 처리
     *
     * @author hyo298, 김재효
     * @param query JPA 쿼리
     * @param pageable 페이징
     */
    private void processDefaultSort(JPAQuery<SearchResponseDTO> query, Pageable pageable) {
        query.orderBy(getSearchOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
    }

    /**
     *
     * 이벤트 데이터 검색 조건을 생성한다.
     * Event Data Where Builder
     *
     * @author hyo298, 김재효
     * @param qEvents 페이징 설정
     * @param searchRequestDTO 검색 조건
     * @return BooleanBuilder
     */
    private BooleanBuilder setSearchCondition(QEvents qEvents, SearchRequestDTO searchRequestDTO) {
        BooleanBuilder builder = new BooleanBuilder();

        // step.1 사전 데이터 in 구성(Text 검색, 거리표준)
        if (searchRequestDTO.hasKeyword()) {
            builder.and(qEvents.eventId.in(searchRequestDTO.getEventIds()));
        }

        // step.2 시작일자, 종료일자
        if (searchRequestDTO.hasDateAllBoolean()) {
            BooleanExpression statCondition = qEvents.operStatDt.loe(searchRequestDTO.getOperEndDt());
            BooleanExpression endCondition = qEvents.operEndDt.goe(searchRequestDTO.getOperStatDt());
            BooleanTemplate dateGroupedCondition = Expressions.booleanTemplate("(({0}) AND ({1}))", statCondition, endCondition);
            builder.and(dateGroupedCondition);
        }

        // step.3 이벤트 구분 코드
        if (searchRequestDTO.hasEventTypeCd()) {
            builder.and(qEvents.eventTypeCd.eq(searchRequestDTO.getEventTypeCd()));
        }

        // step.4 이벤트 삭제 여부
        builder.and(qEvents.deleteYn.eq("N"));

        // step.5 카테고리
        if (searchRequestDTO.hasCategories()) {
            builder.and(qEvents.ctgyId.in(searchRequestDTO.getCategories()));
        }

        // step.6 지역그룹단위필터
        if (searchRequestDTO.hasRegionGroups()) {
            //builder.and(qEvents.ctgyId.in(searchRequestDTO.getRegionGroups()));
        }

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
