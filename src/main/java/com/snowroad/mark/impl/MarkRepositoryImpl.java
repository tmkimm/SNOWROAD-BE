package com.snowroad.mark.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.snowroad.entity.*;
import com.snowroad.mark.domain.MarkRepository;
import com.snowroad.mark.web.dto.MarkSaveRequestDto;
import com.snowroad.mark.web.dto.MarkedEventResponseDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
public class MarkRepositoryImpl implements MarkRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public Page<MarkedEventResponseDTO> getMarkedEventList(Pageable page, Long userId) {
        QEvents e = QEvents.events;
        QMark mark = QMark.mark;
        QView view = QView.view;
        QEventFilesMst fileMst = QEventFilesMst.eventFilesMst;
        QEventFilesDtl fileDtl = QEventFilesDtl.eventFilesDtl;

        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 현재일자를 yyyyMMdd 포맷으로 변환
        OrderSpecifier<?> orderBy = e.operStatDt.asc(); // 기본 정렬

        // ✅ 전체 데이터 개수 조회 (페이징을 위한 count 쿼리)
        long total = queryFactory
                .select(e.count())
                .from(e)
                .join(mark).on(e.eventId.eq(mark.eventId)) // JOIN TB_EVNT_LIKE_D
                .where(
                        mark.userAcntNo.eq(userId),
                        mark.likeYn.eq("Y"))
                .fetchOne();

        List<MarkedEventResponseDTO> result = queryFactory
                .select(
                        e.eventId,
                        e.eventNm,
                        e.eventCntn,
                        e.eventAddr,
                        e.operStatDt.substring(2, 8), // SUBSTRING(e.OPER_STAT_DT,3,6)
                        e.operEndDt.substring(2, 8), // SUBSTRING(e.OPER_END_DT,3,6)
                        e.ctgyId,
                        e.eventTypeCd,
                        mark.likeYn,
                        fileDtl.fileUrl,
                        fileDtl.fileThumbUrl
                )
                .from(e)
                .leftJoin(view).on(e.eventId.eq(view.eventId)) // JOIN TB_EVNT_VIEW_D
                .join(mark).on(e.eventId.eq(mark.eventId)) // JOIN TB_EVNT_LIKE_D
                .leftJoin(fileMst).on(e.eventTumbfile.fileMstId.eq(fileMst.fileMstId)) // JOIN TB_EVNT_FILE_M
                .leftJoin(fileDtl).on(fileMst.fileMstId.eq(fileDtl.fileMst.fileMstId)) // JOIN TB_EVNT_FILE_D
                .where(
                        mark.userAcntNo.eq(userId),
                        mark.likeYn.eq("Y")
//                        eventTypeCd.equals("ALL") ? null : e.eventTypeCd.eq(eventTypeCd)
                )
                .orderBy(orderBy) // ✅ 정렬 적용
                .offset(page.getOffset()) // ✅ 페이지 시작 위치
                .limit(page.getPageSize()) // ✅ 한 페이지당 개수
                .fetch()
                .stream()
                .map(row -> new MarkedEventResponseDTO(
                        row.get(e.eventId),
                        row.get(e.eventNm),
                        row.get(e.eventCntn),
                        row.get(e.eventAddr),
                        row.get(e.operStatDt.substring(2, 8)), // 날짜 변환
                        row.get(e.operEndDt.substring(2, 8)), // 날짜 변환
                        row.get(e.ctgyId),
                        row.get(e.eventTypeCd),
                        row.get(mark.likeYn),
                        row.get(fileDtl.fileUrl),
                        row.get(fileDtl.fileThumbUrl)
                )).toList();
        return new PageImpl<>(result, page, total); // ✅ 페이징된 결과 반환
    }

    @Transactional
    @Override
    public Long addMarkEvent(MarkSaveRequestDto requestDto, Long userId){

        QMark mark = QMark.mark;

        // 1) 넘어온 likeYn 값이 N일 경우, 기존에 'Y'로 저장된 데이터를 N으로 변경
        if ("N".equals(requestDto.getLikeYn())) {
            // 해당 사용자와 이벤트의 기존 데이터를 조회
            Mark existingMark = queryFactory
                    .selectFrom(mark)
                    .where(mark.userAcntNo.eq(userId)
                            .and(mark.eventId.eq(requestDto.getEventId())))
                    .fetchOne();

            if (existingMark != null && "Y".equals(existingMark.getLikeYn())) {
                // 기존에 'Y'인 데이터가 있으면 'N'으로 업데이트
                queryFactory.update(mark)
                        .set(mark.likeYn, "N")
                        .where(mark.userAcntNo.eq(userId)
                                .and(mark.eventId.eq(requestDto.getEventId())))
                        .execute();
                return existingMark.getEventId();  // 기존 데이터의 eventId 반환
            }
        }

        // 2) requestDto eventId를 가져와, 기존에 즐겨찾기를 추가한 항목인지 체크
        Mark existingMark = queryFactory
                .selectFrom(mark)
                .where(mark.userAcntNo.eq(userId)
                        .and(mark.eventId.eq(requestDto.getEventId())))
                .fetchOne();

        if (existingMark == null) {
            // 3) 즐겨찾기 데이터가 없을 시 신규 등록 (INSERT)
            Mark newMark = Mark.builder()
                    .userAcntNo(userId) // 서비스 레이어에서 userId 설정
                    .eventId(requestDto.getEventId())
                    .likeYn(requestDto.getLikeYn())
                    .build();
            entityManager.merge(newMark);
            return newMark.getEventId();
        } else {
            // 4) 기존 즐겨찾기 이력 있을 시 likeYn 업데이트 (UPDATE)
            queryFactory.update(mark)
                    .set(mark.likeYn, requestDto.getLikeYn())
                    .where(mark.userAcntNo.eq(userId)
                            .and(mark.eventId.eq(requestDto.getEventId())))
                    .execute();
            return existingMark.getEventId();
        }

    }

}

