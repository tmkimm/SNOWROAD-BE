package com.snowroad.event.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.entity.*;
import com.snowroad.event.domain.EventsRepositoryCustom;
import com.snowroad.event.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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
                        e.operStatDt,
                        e.operEndDt,
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
                .operStatDt(row.get(e.operStatDt))
                .operEndDt(row.get(e.operEndDt))
                .ctgyId(row.get(e.ctgyId))
                .eventTypeCd(row.get(e.eventTypeCd))
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public Page<DetailEventsResponseDto> getEvntList(Pageable page, String eventTypeCd, String sortType, List<String> ctgyId, String fromDate, String toDate, List<String> geo, Long userId) {

        QEvents e = QEvents.events;
        QMark mark = QMark.mark;
        QView view = QView.view;
        QEventFilesMst fileMst = QEventFilesMst.eventFilesMst;
        QEventFilesDtl fileDtl = QEventFilesDtl.eventFilesDtl;

        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 현재일자를 yyyyMMdd 포맷으로 변환

//        List<String> categoryKeys = ctgyId.stream()
//                .map(Category::getKey) // Enum에서 key 값만 추출
//                .toList();


        // WHERE 조건 동적 추가
        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(e.deleteYn.eq("N")); // 기본 조건 유지 (삭제여부)

        // userId값이 있는 경우 좋아요여부 체킹
//        BooleanExpression markJoinCondition = null;
//        if (userId != null && userId != 0) {
//            userId = 0L;
//        }
        // categoryList 값이 존재할 때만 IN 조건 추가
        if (ctgyId != null && !ctgyId.isEmpty()) {
            whereCondition.and(e.ctgyId.in(ctgyId));
        }
        // geoList 값이 존재할 때만 IN 조건 추가
        if (geo != null && !geo.isEmpty()) {
            whereCondition.and(e.ldcd.in(geo));
        }
        // fromDate & toDate 값이 존재할 때만 BETWEEN 조건 추가
        if (fromDate != null && toDate != null) {
            whereCondition.and(e.operStatDt.between(fromDate, toDate));
        }
        // sortType 10:조회순 20:최신순(오픈일순) 30:마감순(마감임박순) 40:지역별(지역명칭)-미완 50:거리순-미완
        // sortType = 30일 때는 `operEndDt`가 현재일자 이후여야 함
        if ("30".equals(sortType)) {
            whereCondition.and(e.operEndDt.goe(todayStr)); // 현재일자보다 이후 데이터
        }
        // ORDER BY 조건 설정,
        OrderSpecifier<?> orderBy = e.operStatDt.asc(); // 기본 정렬
        if ("10".equals(sortType)) {
            orderBy = view.viewNwvl.desc();
        } else if ("20".equals(sortType)) {
            orderBy = e.operStatDt.desc();
        } else if ("30".equals(sortType)) {
            orderBy = e.operEndDt.asc();
        } else if ("40".equals(sortType)) {
        //    orderBy = e.operEndDt.asc();
        } else if ("50".equals(sortType)) {
        //    orderBy = e.operEndDt.asc();
        }

        // 전체 데이터 개수 조회 (페이징을 위한 count 쿼리)
        Long total = Optional.ofNullable(queryFactory
                        .select(e.count())
                        .from(e)
                        .where(whereCondition)
                        .fetchOne())
                .orElse(0L);

        List<DetailEventsResponseDto> result = queryFactory
                .select(new QDetailEventsResponseDto( // QDetailEventsResponseDto 사용
                        e.eventId,
                        e.eventNm,
                        e.eventCntn,
                        e.eventAddr,
                        Expressions.stringTemplate(
                                "CASE " +
                                        // 마지막 "동" 찾기
                                        "WHEN {0} LIKE '%동%' THEN LEFT({0}, LENGTH({0}) - LOCATE('동', REVERSE({0})) + 1) " +
                                        // 마지막 "구" 찾기
                                        "WHEN {0} LIKE '%구%' THEN LEFT({0}, LENGTH({0}) - LOCATE('구', REVERSE({0})) + 1) " +
                                        // 마지막 "시" 찾기
                                        "WHEN {0} LIKE '%시%' THEN LEFT({0}, LENGTH({0}) - LOCATE('시', REVERSE({0})) + 1) " +
                                        // 마지막 "도" 찾기
                                        "WHEN {0} LIKE '%도%' THEN LEFT({0}, LENGTH({0}) - LOCATE('도', REVERSE({0})) + 1) " +
                                        // 원본 유지
                                        "ELSE {0} " +
                                        "END",
                                e.lnad
                        ),
                        e.operStatDt, // String 타입 유지
                        e.operEndDt, // String 타입 유지
                        e.ctgyId,
                        e.eventTypeCd,
                        mark.likeYn.coalesce("N"),
                        fileDtl.fileUrl,
                        fileDtl.fileThumbUrl,
                        view.viewNwvl.coalesce(0)
                ))
                .from(e)
                .leftJoin(view).on(e.eventId.eq(view.eventId))
                .leftJoin(mark).on(e.eventId.eq(mark.eventId).and(userId != null ? mark.userAcntNo.eq(userId) : Expressions.FALSE))
                .leftJoin(fileMst).on(e.eventTumbfile.fileMstId.eq(fileMst.fileMstId))
                .leftJoin(fileDtl).on(fileMst.fileMstId.eq(fileDtl.fileMst.fileMstId))
                .where(whereCondition)
                .orderBy(orderBy)
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();

        return new PageImpl<>(result, page, total); // 페이징된 결과 반환
    }

    public EventContentsResponseDto findEvntData(Long evntId){

        QEvents e = QEvents.events;
        QMark mark = QMark.mark;
        QView view = QView.view;
        QEventFilesMst fileMst = QEventFilesMst.eventFilesMst;
        QEventFilesDtl fileDtl = QEventFilesDtl.eventFilesDtl;

        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 현재일자를 yyyyMMdd 포맷으로 변환

        Tuple tupleResult = queryFactory
                .select(
                        e.eventId,
                        e.eventNm,
                        e.eventCntn,
                        e.eventAddr,
                        e.rads,
                        e.lnad,
                        e.operStatDt,
                        e.operEndDt,
                        e.operDttmCntn,
                        e.ctgyId,
                        e.eventTypeCd,
                        mark.likeYn.coalesce("N"),
                        view.viewNwvl.coalesce(0),
                        fileDtl.fileUrl,
                        fileDtl.fileThumbUrl
                )
                .from(e)
                .leftJoin(view).on(e.eventId.eq(view.eventId)) // JOIN TB_EVNT_VIEW_D
                .leftJoin(mark).on(e.eventId.eq(mark.eventId)) // JOIN TB_EVNT_LIKE_D
                .leftJoin(fileMst).on(e.eventTumbfile.fileMstId.eq(fileMst.fileMstId)) // JOIN TB_EVNT_FILE_M
                .leftJoin(fileDtl).on(fileMst.fileMstId.eq(fileDtl.fileMst.fileMstId)) // JOIN TB_EVNT_FILE_D
                .where(
                        e.eventId.eq(evntId)
                )
                .fetchOne();
        String likeYn = tupleResult.get(mark.likeYn.coalesce("N"));

        EventContentsResponseDto dto = (tupleResult != null) ? new EventContentsResponseDto(
                tupleResult.get(e.eventId),
                tupleResult.get(e.eventNm),
                tupleResult.get(e.eventCntn),
                tupleResult.get(e.eventAddr),
                tupleResult.get(e.rads),
                tupleResult.get(e.lnad),
                tupleResult.get(e.operStatDt),
                tupleResult.get(e.operEndDt),
                tupleResult.get(e.operDttmCntn),
                tupleResult.get(e.ctgyId),
                tupleResult.get(e.eventTypeCd),
                tupleResult.get(mark.likeYn),
                (tupleResult.get(view.viewNwvl) != null) ? tupleResult.get(view.viewNwvl) : 0, // Null 체크 추가
                tupleResult.get(fileDtl.fileUrl),
                tupleResult.get(fileDtl.fileThumbUrl)
        ) : null;


        return dto;
    }

}

