package com.snowroad.event.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.common.exception.EventNotFoundException;
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
import java.util.ArrayList;
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
                        eventTypeCd.equalsIgnoreCase("ALL") ? null : e.eventTypeCd.eq(eventTypeCd),
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
        QRegion region = QRegion.region;
        QLocalDistrict district = QLocalDistrict.localDistrict;

        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 현재일자를 yyyyMMdd 포맷으로 변환

        // WHERE 조건 동적 추가
        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(e.deleteYn.eq("N")); // 기본 조건 유지 (삭제여부)

        // eventTypeCd 조건 생성
        Predicate eventTypePredicate = null;
        if (!eventTypeCd.equalsIgnoreCase("ALL")) {
            eventTypePredicate = e.eventTypeCd.eq(eventTypeCd);
            whereCondition.and(eventTypePredicate);
        }

        // categoryList 값이 존재할 때만 IN 조건 추가
        if (ctgyId != null && !ctgyId.isEmpty()) {
            whereCondition.and(e.ctgyId.in(ctgyId));
        }

        // fromDate & toDate 값이 존재할 때만 BETWEEN 조건 추가
        if (fromDate != null && toDate != null) {
            whereCondition.and(e.operStatDt.between(fromDate, toDate));
        }
        // geoList 값이 존재할 때만 IN 조건 추가
        if (geo != null && !geo.isEmpty()) {
            whereCondition.and(region.rgntCd.in(geo));
        }
        // sortType 10:조회순 20:최신순(오픈일순) 30:마감순(마감임박순) 40:지역별(지역명칭)-미완
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
            orderBy = e.lnad.asc();
        }

        // 전체 데이터 개수 조회 (페이징을 위한 count 쿼리)
        Long total = Optional.ofNullable(queryFactory
                        .select(e.count())
                        .from(e)
                        .leftJoin(district).on(e.ldcd.eq(district.ldcd))
                        .innerJoin(region).on(district.region.eq(region))  // << 추가
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
                .leftJoin(district).on(e.ldcd.eq(district.ldcd))
                .innerJoin(region).on(district.region.eq(region))
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

    // 썸네일 파일 관련 별칭 생성 (서브쿼리용)
    QEventFilesMst subFileMstThumb = new QEventFilesMst("subFileMstThumb");
    QEventFilesDtl subFileDtlThumb = new QEventFilesDtl("subFileDtlThumb");

    // 메인 파일 관련 별칭 생성 (메인 쿼리 조인용)
    QEventFilesMst fileMstMain = new QEventFilesMst("fileMstMain");
    QEventFilesDtl fileDtlMain = new QEventFilesDtl("fileDtlMain");

    BooleanExpression likeCondition;
    if (userId != null) {
        likeCondition = mark.userAcntNo.eq(userId);
    } else {
        likeCondition = Expressions.FALSE;
    }

    // 쿼리 결과가 여러 행으로 나올 수 있으므로, fetch()를 사용하고 Tuple로 받습니다.
    List<Tuple> results = queryFactory
            .select(
                    e, // Events 엔티티 전체를 가져옵니다 (기본 정보를 쉽게 매핑하기 위함)
                    Expressions.cases() // likeYn 필드에 매핑
                            .when(mark.likeYn.isNotNull()).then(mark.likeYn.stringValue())
                            .otherwise("N"),
                    view.viewNwvl.coalesce(0), // viewNwvl 필드에 매핑 (null이면 0)
                    fileDtlMain.fileUrl,       // 메인 이미지 URL (리스트로 만들 값)
                    // smallImageUrl 필드는 썸네일 파일 마스터 ID를 참조하여 서브쿼리로 단일 값으로 가져옵니다.
                    JPAExpressions.select(subFileDtlThumb.fileUrl) // 썸네일 URL 필드 선택
                            .from(subFileMstThumb)
                            .join(subFileDtlThumb).on(subFileMstThumb.fileMstId.eq(subFileDtlThumb.fileMst.fileMstId))
                            .where(subFileMstThumb.fileMstId.eq(e.eventTumbfile.fileMstId)) // 썸네일 마스터 ID 조건만 사용
                            .orderBy(subFileDtlThumb.fileDtlId.asc()) // 여러개일 경우 첫 번째 상세 레코드 선택
                            .limit(1)
            )
            .from(e)
            .leftJoin(view).on(e.eventId.eq(view.eventId)) // JOIN TB_EVNT_VIEW_D
            .leftJoin(mark).on(e.eventId.eq(mark.eventId).and(likeCondition))
            // 메인 파일 조인 경로 (e.eventFiles는 메인 파일 마스터를 참조)
            .leftJoin(fileMstMain).on(e.eventFiles.fileMstId.eq(fileMstMain.fileMstId))
            .leftJoin(fileDtlMain).on(fileMstMain.fileMstId.eq(fileDtlMain.fileMst.fileMstId))
            // 만약 메인 이미지 Dtl에도 특정 fileType으로 구분해야 한다면 여기에 추가
            // .where(fileDtlMain.fileType.eq("MAIN_IMAGE"))
            .where(
                    e.eventId.eq(evntId)
            )
            .fetch();

    if (results.isEmpty()) {
        return null; // 결과가 없으면 null 반환
    }

    // Tuple 결과를 순회하며 DTO를 만들고 이미지 URL 리스트를 채웁니다.
    EventContentsResponseDto resultDto = null;
    List<String> imageUrl = new ArrayList<>();

    for (Tuple row : results) {
        // 첫 번째 행에서 Event 기본 정보와 단일 필드들을 추출하여 DTO를 초기화합니다.
        if (resultDto == null) {
            Events eventEntity = row.get(e); // Events 엔티티 자체를 가져옴
            String likeYnValue = row.get(1, String.class); // likeYn
            Integer viewNwvlValue = row.get(2, Integer.class); // viewNwvl
            String smallImageUrlValue = row.get(4, String.class); // 썸네일 URL은 5번째 컬럼 (인덱스 4)

            resultDto = new EventContentsResponseDto(
                    eventEntity.getEventId(),
                    eventEntity.getEventNm(),
                    eventEntity.getEventCntn(),
                    eventEntity.getEventAddr(),
                    eventEntity.getRads(),
                    eventEntity.getLnad(),
                    eventEntity.getOperStatDt(),
                    eventEntity.getOperEndDt(),
                    eventEntity.getOperDttmCntn(),
                    eventEntity.getCtgyId(),
                    eventEntity.getEventTypeCd(),
                    likeYnValue,
                    viewNwvlValue,
                    smallImageUrlValue,
                    eventEntity.getEventDetailUrl()
            );
        }

        // 모든 행에서 fileDtlMain.fileUrl을 수집합니다.
        String currentFileUrl = row.get(3, String.class); // fileUrl은 4번째 컬럼 (인덱스 3)
        if (currentFileUrl != null && !imageUrl.contains(currentFileUrl)) { // 중복 방지
            imageUrl.add(currentFileUrl);
        }
    }

    // DTO에 이미지 URL 리스트를 설정합니다.
    if (resultDto != null) {
        resultDto.setImageUrl(imageUrl);
    }

    System.out.println("Result DTO: " + resultDto);
    return resultDto;
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
        QMark mark = QMark.mark;

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

                    // Q-클래스 별칭을 맵 내부에서 선언
                    QEventFilesMst subFileMst = new QEventFilesMst("subFileMst");
                    QEventFilesDtl subFileDtl = new QEventFilesDtl("subFileDtl");

                    // 썸네일 파일 정보를 가져옵니다.
                    if (geoData.getEventTumbfile() != null) { // NullPointerException 방지
                        // 썸네일 URL만 가져오는 서브쿼리
                        String smallImageUrl = queryFactory
                                .select(subFileDtl.fileUrl) // 썸네일 URL 필드 선택 (fileUrl 사용)
                                .from(subFileMst)
                                .leftJoin(subFileDtl).on(subFileMst.fileMstId.eq(subFileDtl.fileMst.fileMstId))
                                .where(subFileMst.fileMstId.eq(geoData.getEventTumbfile().getFileMstId())) // 썸네일 마스터 ID 조건만 사용
                                .orderBy(subFileDtl.fileDtlId.asc()) // 여러 개일 경우 어떤 것을 선택할지 기준
                                .limit(1) // 반드시 단일 값만 가져오도록 제한
                                .fetchOne(); // 이제 안전하게 fetchOne() 사용 가능

                        dto.setSmallImageUrl(smallImageUrl);
                    }
                    // imageUrl (메인 이미지)는 HomeEventsResponseDto에서 필요 없으므로 설정하지 않습니다.

                    return dto;
                })
                .limit(10)
                .collect(Collectors.toList());

        return nearEventsResponseList;

    }

}

