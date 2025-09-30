package com.snowroad.search.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.entity.*;
import com.snowroad.geodata.util.HaversineFormula;
import com.snowroad.search.dto.QSearchResponseDTO;
import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.dto.SearchResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *
 * 검색 & 리스트 조회
 *
 * @author hyo298, 김재효
 * @version 0.0.1
 * @since 2025-05-05
 *
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SearchCustomRepositoryImpl implements SearchCustomRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 이벤트 데이터 검색을 수행한다.
     *
     * @param searchRequest 검색 조건
     * @return List<Long>
     * @author hyo298, 김재효
     */
    @Override
    public Page<SearchResponseDTO> findSearchEventDataList(SearchRequestDTO searchRequest) {

        QEvents qEvents = QEvents.events;
        QEventFilesMst qTumbFileMst = new QEventFilesMst("qTumbFileMst");
        QEventFilesDtl qTumbFileDtl = new QEventFilesDtl("qTumbFileDtl");
        QEventFilesMst qEventFile = new QEventFilesMst("eventFile");

        QMark qMark = new QMark("mark");
        QEventView qEventView = QEventView.eventView;
        BooleanBuilder builder = setSearchCondition(qEvents, searchRequest);

        Long userAcntNo = searchRequest.getUserAcntNo();
        Expression<String> likeYnExpr;
        if (userAcntNo != null) {
            BooleanExpression likedExists =
                    JPAExpressions.selectOne()
                            .from(qMark)
                            .where(
                                    qMark.eventId.eq(qEvents.eventId)      // ← 역방향 매핑 없으니 컬럼으로 연결
                                            .and(qMark.userAcntNo.eq(userAcntNo))  // 사용자 한정
                                            .and(qMark.likeYn.eq("Y"))
                            )
                            .exists();

            likeYnExpr = new CaseBuilder()
                    .when(likedExists).then("Y")
                    .otherwise("N");
        } else {
            // 정책에 맞게 null 또는 "N"
            likeYnExpr = Expressions.constant("N");
        }

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
                        qTumbFileDtl.fileUrl,
                        qEventFile.fileMstId,
                        qEventView.viewNmvl,
                        likeYnExpr
                ))
                .from(qEvents)
                .leftJoin(qEvents.eventTumbfile, qTumbFileMst) // 이벤트 → 썸네일 마스터
                .leftJoin(qTumbFileMst.eventFilesDtlList, qTumbFileDtl)
                .leftJoin(qEvents.eventFiles, qEventFile)
                .leftJoin(qEvents.eventView, qEventView)
                .where(builder);

        String sortType = searchRequest.getSortType();
        Sort sort = switch (sortType) {
            //조회순, 최신순, 마감순
            case "10" -> Sort.by(Sort.Direction.DESC, "eventView.viewNmvl");
            case "20" -> Sort.by(Sort.Direction.DESC, "operStatDt");
            case "30" -> Sort.by(Sort.Direction.DESC, "operEndDt");
            default -> Sort.by(Sort.Direction.DESC, "operStatDt");
        };

        int page = searchRequest.getPage();
        int pageSize = searchRequest.getPageSize();

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        boolean isDistanceSort = "거리순".equalsIgnoreCase(searchRequest.getSortType());
        if (!isDistanceSort) {
            searchQuery.orderBy(getSearchOrderSpecifiers(pageable.getSort()))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());
        }

        List<SearchResponseDTO> events = searchQuery.fetch();
        if (searchRequest.hasCoordinate()) {
            for (SearchResponseDTO dto : events) {
                if (dto.getAddrLttd() != null && dto.getAddrLotd() != null) {
                    double dist = HaversineFormula.calculateDistance(
                            searchRequest.getLatitude(),
                            searchRequest.getLongitude(),
                            dto.getAddrLttd(),
                            dto.getAddrLotd()
                    );
                    dto.setDistanceKm(dist);
                    dto.setDisplayDistance(dist < 1.0 ? Math.round(dist * 1000) + "m" : Math.round(dist * 10) / 10.0 + "km");
                }
            }
        }

        if (isDistanceSort) {
            List<SearchResponseDTO> sortedList = events.stream()
                    .filter(dto -> dto.getDistanceKm() != null)
                    .sorted(Comparator.comparingDouble(SearchResponseDTO::getDistanceKm).reversed())
                    .toList();

            int start = Math.min((int) pageable.getOffset(), sortedList.size());
            int end = Math.min(start + pageable.getPageSize(), sortedList.size());
            List<SearchResponseDTO> pagedList = sortedList.subList(start, end);
            return new PageImpl<>(pagedList, pageable, sortedList.size());
        } else {
            long totalCount = countSearchEvents(qEvents, builder);
            return new PageImpl<>(events, pageable, totalCount);
        }
    }

    private long countSearchEvents(QEvents qEvents, BooleanBuilder builder) {
        return Optional.ofNullable(
                queryFactory.select(qEvents.count())
                        .from(qEvents)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);
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

        // 사전 데이터 in 구성(Text 검색, 거리표준)
        BooleanBuilder keywordOr = new BooleanBuilder();
        if (searchRequestDTO.hasKeyword()) {
            builder.and(eventIdInIfPresent(qEvents, searchRequestDTO.getEventIds()));

            String keyword = searchRequestDTO.getKeyword();
            keywordOr.or(qEvents.eventNm.contains(keyword))
                     .or(qEvents.eventCntn.contains(keyword));
        }

        // 시작일자, 종료일자
        if (searchRequestDTO.hasDateAllBoolean()) {
            BooleanExpression statCondition = qEvents.operStatDt.loe(searchRequestDTO.getOperEndDt());
            BooleanExpression endCondition = qEvents.operEndDt.goe(searchRequestDTO.getOperStatDt());
            builder.and(statCondition.and(endCondition));
        } else {
            //이벤트 서버 시간
            String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            builder.and(qEvents.operEndDt.goe(today));
        }

        //검색 보정
        //LIKE 문
        //임시처리
        if (keywordOr.hasValue()) {
            builder.and(keywordOr);
        }

        // 이벤트 구분 코드
        if (searchRequestDTO.hasEventTypeCd() && !Objects.equals(searchRequestDTO.getEventTypeCd(), "all")) {
            builder.and(qEvents.eventTypeCd.eq(searchRequestDTO.getEventTypeCd()));
        }

        // 이벤트 삭제 여부
        builder.and(qEvents.deleteYn.eq("N"));

        // 카테고리
        if (searchRequestDTO.hasCategories()) {
            builder.and(qEvents.ctgyId.in(searchRequestDTO.getCategories()));
        }

        // 지역그룹단위필터
        if (searchRequestDTO.hasRegionGroups()) {
            builder.and(qEvents.localDistrict.tbRgntDeltC.rgntDeltCd.in(searchRequestDTO.getRegionGroups()));
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

    private BooleanExpression eventIdInIfPresent(QEvents qEvents, List<Long> ids) {
        return (ids == null || ids.isEmpty()) ? null : qEvents.eventId.in(ids);
    }
}
