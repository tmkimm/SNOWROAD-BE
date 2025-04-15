package com.snowroad.event.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.config.auth.dto.CustomUserDetails;
import com.snowroad.entity.*;
import com.snowroad.event.domain.EventsRepositoryCustom;
import com.snowroad.event.web.dto.*;
import com.snowroad.geodata.interfaces.GeoDataInterface;
import com.snowroad.geodata.service.GeoDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventsRepositoryImpl implements EventsRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final GeoDataInterface geoDataInterface; // GeoDataInterface 의존성 주입, 인근 컨텐츠 조회용

    @Override
    public List<HomeEventsResponseDto> getMainRcmnList(String eventTypeCd, CustomUserDetails userDetails) {
        QEvents e = QEvents.events;
        QView evd = QView.view;
        QMark eld = QMark.mark;
        QEventFilesMst efm = QEventFilesMst.eventFilesMst;
        QEventFilesDtl efd = QEventFilesDtl.eventFilesDtl;
        QUserCategory uct = QUserCategory.userCategory;

        Long userId = userDetails != null ? userDetails.getUserId() : null;

        BooleanBuilder likeCondition = new BooleanBuilder();
        if (userId != null) {
            likeCondition.and(eld.userAcntNo.eq(userId));
        }

        List<Tuple> result = queryFactory
                .select(
                        e.eventId,
                        e.eventNm,
                        e.operStatDt,
                        e.operEndDt,
                        e.ctgyId,
                        e.eventTypeCd,
                        Expressions.cases()
                                .when(eld.likeYn.isNotNull()).then(eld.likeYn.stringValue())
                                .otherwise("N").as("likeYn"),
                        efd.fileUrl,
                        efd.fileThumbUrl
                )
                .from(e)
                .leftJoin(evd).on(e.eventId.eq(evd.eventId))
                .leftJoin(eld).on(e.eventId.eq(eld.eventId).and(likeCondition))
                .leftJoin(efm).on(e.eventTumbfile.fileMstId.eq(efm.fileMstId))
                .leftJoin(efd).on(efm.fileMstId.eq(efd.fileMst.fileMstId))
                .where(
                        e.deleteYn.eq("N"),
                        e.operEndDt.goe(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))),
                        e.operStatDt.loe(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))),
                        eventTypeCd.equals("ALL") ? null : e.eventTypeCd.eq(eventTypeCd),
                        userId == null ? null : e.ctgyId.in(
                                queryFactory
                                        .select(uct.category.stringValue())
                                        .from(uct)
                                        .where(uct.user.userAccountNo.eq(userId)) // QUser의 id로 비교 (QUser에 id 필드가 있다고 가정)
                        )
                )
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc(), e.operStatDt.asc()) //  RAND() 함수 사용
                .limit(12)
                .fetch();

        return result.stream().map(row -> {
                    HomeEventsResponseDto evntRcmnList = new HomeEventsResponseDto();
                    evntRcmnList.setEventId(row.get(e.eventId));
                    evntRcmnList.setEventNm(row.get(e.eventNm));
                    evntRcmnList.setOperStatDt(row.get(e.operStatDt));
                    evntRcmnList.setOperEndDt(row.get(e.operEndDt));
                    evntRcmnList.setCtgyId(row.get(e.ctgyId));
                    evntRcmnList.setEventTypeCd(row.get(e.eventTypeCd));
                    evntRcmnList.setLikeYn(row.get(Expressions.stringPath("likeYn")));
                    evntRcmnList.setImageUrl(row.get(efd.fileUrl));
                    evntRcmnList.setSmallImageUrl(row.get(efd.fileThumbUrl));
                    return evntRcmnList;
                })
                .collect(Collectors.toList());
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

    public EventContentsResponseDto findEvntData(Long evntId, Long userId){

        QEvents e = QEvents.events;
        QMark mark = QMark.mark;
        QView view = QView.view;
        QEventFilesMst fileMst = QEventFilesMst.eventFilesMst;
        QEventFilesDtl fileDtl = QEventFilesDtl.eventFilesDtl;

        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 현재일자를 yyyyMMdd 포맷으로 변환

        BooleanBuilder likeCondition = new BooleanBuilder();
        if (userId != null) {
            likeCondition.and(mark.userAcntNo.eq(userId));
        }

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
                        Expressions.cases()
                                .when(mark.likeYn.isNotNull()).then(mark.likeYn.stringValue())
                                .otherwise("N").as("likeYn"),
                        view.viewNwvl.coalesce(0),
                        fileDtl.fileUrl,
                        fileDtl.fileThumbUrl
                )
                .from(e)
                .leftJoin(view).on(e.eventId.eq(view.eventId)) // JOIN TB_EVNT_VIEW_D
                .leftJoin(mark).on(e.eventId.eq(mark.eventId).and(likeCondition))
                .leftJoin(fileMst).on(e.eventTumbfile.fileMstId.eq(fileMst.fileMstId)) // JOIN TB_EVNT_FILE_M
                .leftJoin(fileDtl).on(fileMst.fileMstId.eq(fileDtl.fileMst.fileMstId)) // JOIN TB_EVNT_FILE_D
                .where(
                        e.eventId.eq(evntId)
                )
                .fetchOne();
//        String likeYn = tupleResult.get(mark.likeYn.coalesce("N"));

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
                tupleResult.get(Expressions.stringPath("likeYn")),
                (tupleResult.get(view.viewNwvl) != null) ? tupleResult.get(view.viewNwvl) : 0, // Null 체크 추가
                tupleResult.get(fileDtl.fileUrl),
                tupleResult.get(fileDtl.fileThumbUrl)
        ) : null;


        return dto;
    }


    @Override
    public List<HomeEventsResponseDto> getNearEvntList(Long eventId) {

        List<Events> geoDataList = geoDataInterface.getGeoData(eventId);
        String targetEventTypeCd;

        // 입력받은 eventId와 같은 이벤트의 eventTypeCd 찾기
        Optional<Events> targetEventOptional = geoDataList.stream()
                .filter(event -> eventId.equals(event.getEventId()))
                .findFirst();

        if (targetEventOptional.isPresent()) {
            targetEventTypeCd = targetEventOptional.get().getEventTypeCd();
        } else {
            // 해당 eventId의 이벤트가 없으면 빈 리스트 반환 또는 예외 처리
            System.err.println("인근 팝업/전시가 없어요.");
            return List.of(); // 빈 리스트 반환
        }

        // 운영종료일 필터링 위한 오늘 날짜 가져오기
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 사진파일등 연관 데이터를 가져오기 위한 queryDsl 처리
        QEvents e = QEvents.events;
        QMark mark = QMark.mark; // QMark 추가
        QEventFilesMst fileMst = QEventFilesMst.eventFilesMst;
        QEventFilesDtl fileDtl = QEventFilesDtl.eventFilesDtl;
        
//        List<HomeEventsResponseDto> nearEventsResponseList = geoDataList.stream()
//                .filter(geoData -> targetEventTypeCd.equals(geoData.getEventTypeCd())) // eventTypeCd 일치
//                .filter(geoData -> !eventId.equals(geoData.getEventId())) // eventId가 일치하는 데이터 제외
//                .filter(geoData -> {
//                    String operEndDtStr = geoData.getOperEndDt();
//                    if (operEndDtStr != null && operEndDtStr.length() == 8) {
//                        try {
//                            LocalDate endDate = LocalDate.parse(operEndDtStr, dateFormatter);
//                            return !endDate.isBefore(today); // 종료일이 오늘 이후인 데이터만
//                        } catch (DateTimeParseException e) {
//                            System.err.println("operEndDt format error: " + operEndDtStr + " for event ID: " + geoData.getEventId());
//                            return false;
//                        }
//                    }
//                    return false; // operEndDt가 null이거나 형식이 맞지 않으면 제외
//                })
//                .map(geoData -> {
//                    HomeEventsResponseDto dto = new HomeEventsResponseDto();
//                    // Events 엔티티에서 필요한 정보를 HomeEventsResponseDto에 매핑
//                    dto.setEventId(geoData.getEventId());
//                    dto.setEventNm(geoData.getEventNm());
//                    dto.setOperStatDt(geoData.getOperStatDt());
//                    dto.setOperEndDt(geoData.getOperEndDt());
//                    dto.setCtgyId(geoData.getCtgyId());
//                    dto.setEventTypeCd(geoData.getEventTypeCd());
//                    dto.setImageUrl(geoData.getEventFiles().toString());
//                    dto.setSmallImageUrl(geoData.getEventTumbfile().toString());
//                    return dto;
//                })
//                .limit(10) // 최대 10개의 결과만 가져오기
//                .collect(Collectors.toList());

        List<HomeEventsResponseDto> nearEventsResponseList = geoDataList.stream()
                .filter(geoData -> targetEventTypeCd.equals(geoData.getEventTypeCd())) // eventTypeCd 일치
                .filter(geoData -> !eventId.equals(geoData.getEventId())) // eventId가 일치하는 데이터 제외
                .filter(geoData -> {
                    String operEndDtStr = geoData.getOperEndDt();
                    if (operEndDtStr != null && operEndDtStr.length() == 8) {
                        try {
                            LocalDate endDate = LocalDate.parse(operEndDtStr, dateFormatter);
                            return !endDate.isBefore(today); // 종료일이 오늘 이후인 데이터만
                        } catch (DateTimeParseException ex) {
                            System.err.println("operEndDt format error: " + operEndDtStr + " for event ID: " + geoData.getEventId());
                            return false;
                        }
                    }
                    return false; // operEndDt가 null이거나 형식이 맞지 않으면 제외
                })
                .map(geoData -> {
                    HomeEventsResponseDto dto = new HomeEventsResponseDto();
                    dto.setEventId(geoData.getEventId());
                    dto.setEventNm(geoData.getEventNm());
                    dto.setOperStatDt(geoData.getOperStatDt());
                    dto.setOperEndDt(geoData.getOperEndDt());
                    dto.setCtgyId(geoData.getCtgyId());
                    dto.setEventTypeCd(geoData.getEventTypeCd());

                    // 이미지 URL 및 썸네일 URL 추가 조회 (각 이벤트별로 쿼리 실행)
                    if (geoData.getEventTumbfile() != null) { // NullPointerException 방지
                        Tuple fileInfo = queryFactory
                                .select(fileDtl.fileUrl, fileDtl.fileThumbUrl)
                                .from(fileMst)
                                .leftJoin(fileDtl).on(fileMst.fileMstId.eq(fileDtl.fileMst.fileMstId))
                                .where(fileMst.fileMstId.eq(geoData.getEventTumbfile().getFileMstId()))
                                .fetchOne();

                        if (fileInfo != null) {
                            dto.setImageUrl(fileInfo.get(fileDtl.fileUrl));
                            dto.setSmallImageUrl(fileInfo.get(fileDtl.fileThumbUrl));
                        }
                    }

                    return dto;
                })
                .limit(10)
                .collect(Collectors.toList());

        return nearEventsResponseList;

    }

}

