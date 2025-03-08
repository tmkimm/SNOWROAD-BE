package com.snowroad.event.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.BooleanBuilder;
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

    @Override
    public Page<DetailEventsResponseDto> getEvntList(Pageable page, String eventTypeCd, String sortType, List<String> ctgyId, String fromDate, String toDate, List<String> geo) {

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


        // ✅ 전체 데이터 개수 조회 (페이징을 위한 count 쿼리)
        long total = queryFactory
                .select(e.count())
                .from(e)
                .where(whereCondition)
                .fetchOne();

        List<DetailEventsResponseDto> result = queryFactory
                .select(
                        e.eventId,
                        e.eventNm,
                        e.eventCntn,
                        e.eventAddr,
                        e.operStatDt.substring(2, 8), // SUBSTRING(e.OPER_STAT_DT,3,6)
                        e.operEndDt.substring(2, 8), // SUBSTRING(e.OPER_END_DT,3,6)
                        e.ctgyId,
                        e.eventTypeCd,
                        mark.likeYn.coalesce("N"),
                        fileDtl.fileUrl,
                        fileDtl.fileThumbUrl
                )
                .from(e)
                .leftJoin(view).on(e.eventId.eq(view.eventId)) // JOIN TB_EVNT_VIEW_D
                .leftJoin(mark).on(e.eventId.eq(mark.eventId)) // JOIN TB_EVNT_LIKE_D
                .leftJoin(fileMst).on(e.eventTumbfile.fileMstId.eq(fileMst.fileMstId)) // JOIN TB_EVNT_FILE_M
                .leftJoin(fileDtl).on(fileMst.fileMstId.eq(fileDtl.fileDtlId)) // JOIN TB_EVNT_FILE_D
//                .where(
//                        e.deleteYn.eq("N"),
//                        e.ctgyId.in(ctgyId), // ✅ categoryList를 IN 조건으로 적용
//                        "20".equals(sortType) ? e.operStatDt.goe(todayStr) : null, // ✅ sortType이 20이면 operStatDt >= 현재일자
//                        "30".equals(sortType) ? e.operEndDt.goe(todayStr) : null   // ✅ sortType이 30이면 operEndDt >= 현재일자
//                ) // WHERE e.DELT_YN = 'N'
                .where(whereCondition) // ✅ 동적으로 WHERE 조건 적용
                .orderBy(orderBy) // ✅ 정렬 적용
                .offset(page.getOffset()) // ✅ 페이지 시작 위치
                .limit(page.getPageSize()) // ✅ 한 페이지당 개수
                .fetch()
                .stream()
                .map(row -> new DetailEventsResponseDto(
                        row.get(e.eventId),
                        row.get(e.eventNm),
                        row.get(e.eventCntn),
                        row.get(e.eventAddr),
                        row.get(e.operStatDt.substring(2, 8)), // 날짜 변환
                        row.get(e.operEndDt.substring(2, 8)), // 날짜 변환
                        row.get(e.ctgyId),
                        row.get(e.eventTypeCd),
                        row.get(mark.likeYn.charAt(0)), // 임시 char처리, queryDsl 리팩토링 완료시 String으로 Dto타입 변경 필요
                        row.get(fileDtl.fileUrl),
                        row.get(fileDtl.fileThumbUrl)
                )).toList();

        return new PageImpl<>(result, page, total); // ✅ 페이징된 결과 반환
    }
}

