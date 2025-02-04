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

    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, e.EVNT_CNTN as eventCntn, e.EVNT_ADDR AS eventAddr, " +
            "e.OPER_STAT_DT as operStatDt, e.OPER_END_DT as operEndDt, e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, " +
            "e.TUMB_FILE_ID as tumbFileId, evd.VIEW_NMVL, eld.LIKE_YN " +
            "from TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "ORDER BY CASE " +
            "WHEN :sortType = '10' THEN evd.VIEW_NMVL " +
            "WHEN :sortType = '20' THEN e.OPER_STAT_DT " +
            "WHEN :sortType = '30' THEN e.OPER_END_DT " +
            "ELSE e.OPER_STAT_DT END"
            , nativeQuery = true)
    List<Object[]> getEvntList(@Param("sortType") String sortType);

    @Query(value = "SELECT * from TB_EVNT_M e " +
            "WHERE e.EVNT_ID = :eventId", nativeQuery = true)
    List<Events> findEvntData(@Param("eventId") Long eventId);

    // Main Page 인기 팝업/전시 list 조회
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, e.EVNT_CNTN as eventCntn, e.EVNT_ADDR AS eventAddr, " +
            "e.OPER_STAT_DT as operStatDt, e.OPER_END_DT as operEndDt, e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, " +
            "e.TUMB_FILE_ID as tumbFileId, evd.VIEW_NMVL, eld.LIKE_YN " +
            "from TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "WHERE STR_TO_DATE(e.OPER_END_DT, '%Y%m%d') >= CURDATE() " +
            "  AND (:eventTypeCd = 'all' OR e.EVNT_TYPE_CD = :eventTypeCd) " +
            "ORDER BY evd.VIEW_NMVL DESC, OPER_STAT_DT ASC " +
            "LIMIT 10"
            , nativeQuery = true)
    List<Object[]> getMainRankList(@Param("eventTypeCd") String eventTypeCd);

    // 오픈임박-상위 10개 조회순, 종료일자 이전 데이터 추출할 것
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, e.EVNT_CNTN as eventCntn, e.EVNT_ADDR AS eventAddr, " +
            "e.OPER_STAT_DT as operStatDt, e.OPER_END_DT as operEndDt, e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, " +
            "e.TUMB_FILE_ID as tumbFileId, evd.VIEW_NMVL, eld.LIKE_YN " +
            "from TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "WHERE STR_TO_DATE(e.OPER_STAT_DT, '%Y%m%d') >= CURDATE() " +
            "  AND (:eventTypeCd = 'all' OR e.EVNT_TYPE_CD = :eventTypeCd) " +
            "ORDER BY e.OPER_STAT_DT ASC " +
            "LIMIT 10"
            , nativeQuery = true)
    List<Object[]> getMainOperStatList(@Param("eventTypeCd") String eventTypeCd);

    // 종료임박-상위 5개 조회순, 종료일자 이전 데이터 추출할 것
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, e.EVNT_CNTN as eventCntn, e.EVNT_ADDR AS eventAddr, " +
            "e.OPER_STAT_DT as operStatDt, e.OPER_END_DT as operEndDt, e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, " +
            "e.TUMB_FILE_ID as tumbFileId, evd.VIEW_NMVL, eld.LIKE_YN " +
            "FROM TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "WHERE STR_TO_DATE(e.OPER_END_DT, '%Y%m%d') >= CURDATE() " +
            "  AND STR_TO_DATE(e.OPER_STAT_DT, '%Y%m%d') <= CURDATE() " +
            "  AND (:eventTypeCd = 'all' OR e.EVNT_TYPE_CD = :eventTypeCd) " +
            "ORDER BY e.OPER_END_DT ASC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Object[]> getMainOperEndList(@Param("eventTypeCd") String eventTypeCd);

    // 즐겨찾기 항목, 기간이 끝난 항목에 대한 추가처리 필요 (후순위 작업)
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, e.EVNT_CNTN as eventCntn, e.EVNT_ADDR AS eventAddr, " +
            "e.OPER_STAT_DT as operStatDt, e.OPER_END_DT as operEndDt, e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, " +
            "e.TUMB_FILE_ID as tumbFileId, evd.VIEW_NMVL, eld.LIKE_YN " +
            "FROM TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "WHERE eld.LIKE_YN = 'Y' " +
            "ORDER BY eld.DATA_EDIT_DTTM DESC ", nativeQuery = true)
    List<Object[]> findEvntMarkedList();

}
