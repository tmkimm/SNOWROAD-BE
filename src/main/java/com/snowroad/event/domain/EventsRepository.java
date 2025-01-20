package com.snowroad.event.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long> {

    @Query(value = "SELECT * from TB_EVNT_M e ORDER BY e.EVNT_ID DESC", nativeQuery = true)
    List<Events> findAllDesc();

    // Native Query로 Event와 관련된 EventFilesDtl 조회 (eventId 기준)
    @Query(value = "SELECT efd.FILE_DTL_ID AS fileDtlId, efd.FILE_URL as fileUrl, efd.ORIG_FILE_NM as origFileNm FROM TB_EVNT_M e " +
            "JOIN TB_EVNT_FILE_M efm ON e.EVNT_FILE_ID = efm.FILE_MST_ID " +
            "JOIN TB_EVNT_FILE_D efd ON efm.FILE_MST_ID = efd.FILE_MST_ID " +
            "WHERE e.EVNT_ID = :eventId", nativeQuery = true)
    List<Object[]> findEventFilesDtlByEventId(@Param("eventId") Long eventId);

    @Query(value = "SELECT efd.FILE_DTL_ID AS fileDtlId, efd.FILE_URL as fileUrl, efd.ORIG_FILE_NM as origFileNm FROM TB_EVNT_M e " +
            "JOIN TB_EVNT_FILE_M efm ON e.TUMB_FILE_ID = efm.FILE_MST_ID " +
            "JOIN TB_EVNT_FILE_D efd ON efm.FILE_MST_ID = efd.FILE_MST_ID " +
            "WHERE e.EVNT_ID = :eventId", nativeQuery = true)
    List<Object[]> findTumbFilesDtlByEventId(@Param("eventId") Long eventId);

    @Query(value = "SELECT * from TB_EVNT_M e ORDER BY e.OPER_END_DT desc", nativeQuery = true)
    List<Events> findEvntList();

    @Query(value = "SELECT * from TB_EVNT_M e " +
            "WHERE e.EVNT_ID = :eventId", nativeQuery = true)
    List<Events> findEvntData(@Param("eventId") Long eventId);

    // 상위 10개 조회순, 종료일자 이전 데이터 확인 후 추출
    @Query(value = "SELECT * from TB_EVNT_M e " +
            "JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "WHERE STR_TO_DATE(e.OPER_END_DT, '%Y%m%d') > CURDATE() " +
            "ORDER BY evd.VIEW_NMVL DESC " +
            "LIMIT 10"
            , nativeQuery = true)
    List<Events> getMainRankList();

    // 상위 10개 조회순, 종료일자 이전 데이터 추출할 것
    @Query(value = "SELECT * from TB_EVNT_M  "
            , nativeQuery = true)
    List<Events> getMainOperStatList();

    // 상위 10개 조회순, 종료일자 이전 데이터 추출할 것
    @Query(value = "SELECT * from TB_EVNT_M e "
            , nativeQuery = true)
    List<Events> getMainOperEndList();

    // 즐겨찾기 항목, 기간이 끝난 항목에 대한 추가처리 필요
    @Query(value = "SELECT * from TB_EVNT_M e " +
            "JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "WHERE eld.LIKE_YN = 'Y' " +
            "ORDER BY eld.DATA_EDIT_DTTM DESC ", nativeQuery = true)
    List<Events> findEvntMarkedList();

}
