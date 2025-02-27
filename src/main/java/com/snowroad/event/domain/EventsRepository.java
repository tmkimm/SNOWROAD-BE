package com.snowroad.event.domain;

import com.snowroad.entity.Events;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Primary  // ✅ 기본 Repository로 지정
public interface EventsRepository extends JpaRepository<Events, Long>, EventsRepositoryCustom{

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

    @Query(value = "SELECT * from TB_EVNT_M e " +
            "WHERE e.EVNT_ID = :eventId", nativeQuery = true)
    List<Events> findEvntData(@Param("eventId") Long eventId);

    // 메인페이지 최상단 배너 컨텐츠 조회
    // 현재는 가장 최근 등록된 컨텐츠순 조회, 추후 해당 배너 활용방법 따라 재조정 필요
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, SUBSTRING(e.OPER_STAT_DT,3,6) as operStatDt, SUBSTRING(e.OPER_END_DT,3,6) as operEndDt, " +
            "e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, IFNULL(eld.LIKE_YN,'N') AS LIKE_YN, " +
            "efd.FILE_URL as imageUrl, efd.FILE_THUB_URL as smALLImageUrl " +
            "from TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_M efm ON e.TUMB_FILE_ID = efm.FILE_MST_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_D efd ON efm.FILE_MST_ID = efd.FILE_MST_ID " +
            "WHERE STR_TO_DATE(e.OPER_END_DT, '%Y%m%d') >= CURDATE() " +
            "  AND (:eventTypeCd = 'ALL' OR e.EVNT_TYPE_CD = :eventTypeCd) " +
            "ORDER BY e.DATA_CRTN_DTTM DESC " + // RAND() * 10000 => 랜덤정렬
            "LIMIT 10"
            , nativeQuery = true)
    List<Object[]> getMainBannerList(@Param("eventTypeCd") String eventTypeCd);


    // List<Events> getMainTestList(@Param("eventTypeCd") String eventTypeCd);

    // 메인페이지 인기 컨텐츠 list 조회
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, SUBSTRING(e.OPER_STAT_DT,3,6) as operStatDt, SUBSTRING(e.OPER_END_DT,3,6) as operEndDt, " +
            "e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, IFNULL(eld.LIKE_YN,'N') AS LIKE_YN, " +
            "efd.FILE_URL as imageUrl, efd.FILE_THUB_URL as smALLImageUrl " +
            "from TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_M efm ON e.TUMB_FILE_ID = efm.FILE_MST_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_D efd ON efm.FILE_MST_ID = efd.FILE_MST_ID " +
            "WHERE e.DELT_YN = 'N' AND STR_TO_DATE(e.OPER_END_DT, '%Y%m%d') >= CURDATE() " +
            "  AND (:eventTypeCd = 'ALL' OR e.EVNT_TYPE_CD = :eventTypeCd) " +
            "ORDER BY evd.VIEW_NMVL DESC, OPER_STAT_DT ASC " +
            "LIMIT 10"
            , nativeQuery = true)
    List<Object[]> getMainRankList(@Param("eventTypeCd") String eventTypeCd);


    // 메인페이지 추천 컨텐츠 list 조회 (카테고리 테이블 완성 후 사용자 맞춤 추가작업 진행 필요)
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, SUBSTRING(e.OPER_STAT_DT,3,6) as operStatDt, SUBSTRING(e.OPER_END_DT,3,6) as operEndDt, " +
            "e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, IFNULL(eld.LIKE_YN,'N') AS LIKE_YN, " +
            "efd.FILE_URL as imageUrl, efd.FILE_THUB_URL as smALLImageUrl " +
            "from TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_M efm ON e.TUMB_FILE_ID = efm.FILE_MST_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_D efd ON efm.FILE_MST_ID = efd.FILE_MST_ID " +
            "WHERE e.DELT_YN = 'N' AND STR_TO_DATE(e.OPER_END_DT, '%Y%m%d') >= CURDATE() " +
            "  AND STR_TO_DATE(e.OPER_STAT_DT, '%Y%m%d') <= CURDATE() " +
            "  AND (:eventTypeCd = 'ALL' OR e.EVNT_TYPE_CD = :eventTypeCd) " +
            "ORDER BY RAND() * 10000, OPER_STAT_DT ASC " + // RAND() * 10000 => 랜덤정렬
            "LIMIT 10"
            , nativeQuery = true)
    List<Object[]> getMainRcmnList(@Param("eventTypeCd") String eventTypeCd);

    // 메인페이지 오픈임박-상위 9개 조회순
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, SUBSTRING(e.OPER_STAT_DT,3,6) as operStatDt, SUBSTRING(e.OPER_END_DT,3,6) as operEndDt, " +
            "e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, IFNULL(eld.LIKE_YN,'N') AS LIKE_YN, " +
            "efd.FILE_URL as imageUrl, efd.FILE_THUB_URL as smALLImageUrl, " +
            "CONCAT('D-', DATEDIFF(STR_TO_DATE(e.OPER_STAT_DT, '%Y%m%d'), CURRENT_DATE())) AS D_DAY " +
            "from TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_M efm ON e.TUMB_FILE_ID = efm.FILE_MST_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_D efd ON efm.FILE_MST_ID = efd.FILE_MST_ID " +
            "WHERE e.DELT_YN = 'N' AND STR_TO_DATE(e.OPER_STAT_DT, '%Y%m%d') >= CURDATE() " +
            "  AND (:eventTypeCd = 'ALL' OR e.EVNT_TYPE_CD = :eventTypeCd) " +
            "ORDER BY e.OPER_STAT_DT ASC " +
            "LIMIT 9"
            , nativeQuery = true)
    List<Object[]> getMainOperStatList(@Param("eventTypeCd") String eventTypeCd);

    // 종료임박-상위 9개 조회순
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, SUBSTRING(e.OPER_STAT_DT,3,6) as operStatDt, SUBSTRING(e.OPER_END_DT,3,6) as operEndDt, " +
            "e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, IFNULL(eld.LIKE_YN,'N') AS LIKE_YN, " +
            "efd.FILE_URL as imageUrl, efd.FILE_THUB_URL as smALLImageUrl, " +
            "CONCAT('D-', DATEDIFF(STR_TO_DATE(e.OPER_END_DT, '%Y%m%d'), CURRENT_DATE())) AS D_DAY " +
            "from TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_M efm ON e.TUMB_FILE_ID = efm.FILE_MST_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_D efd ON efm.FILE_MST_ID = efd.FILE_MST_ID " +
            "WHERE e.DELT_YN = 'N' AND STR_TO_DATE(e.OPER_END_DT, '%Y%m%d') >= CURDATE() " +
            "  AND STR_TO_DATE(e.OPER_STAT_DT, '%Y%m%d') <= CURDATE() " +
            "  AND (:eventTypeCd = 'ALL' OR e.EVNT_TYPE_CD = :eventTypeCd) " +
            "ORDER BY e.OPER_END_DT ASC " +
            "LIMIT 9",
            nativeQuery = true)
    List<Object[]> getMainOperEndList(@Param("eventTypeCd") String eventTypeCd);

    // 즐겨찾기 항목, 기간이 끝난 항목에 대한 추가처리 필요 (후순위 작업)
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, e.EVNT_CNTN as eventCntn, e.EVNT_ADDR AS eventAddr, " +
            "SUBSTRING(e.OPER_STAT_DT,3,6) as operStatDt, SUBSTRING(e.OPER_END_DT,3,6) as operEndDt, e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd, " +
            "e.TUMB_FILE_ID as tumbFileId, evd.VIEW_NMVL, IFNULL(eld.LIKE_YN,'N') AS LIKE_YN " +
            "FROM TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "WHERE e.DELT_YN = 'N' AND IFNULL(eld.LIKE_YN,'N') AS LIKE_YN = 'Y' " +
            "ORDER BY eld.DATA_EDIT_DTTM DESC ", nativeQuery = true)
    List<Object[]> findEvntMarkedList();

    // 리스트 페이지 컨텐츠 목록 조회
    @Query(value = "SELECT e.EVNT_ID as eventId, e.EVNT_NM AS eventNm, e.EVNT_CNTN as eventCntn, " +
            "e.EVNT_ADDR AS eventAddr, " +
            "SUBSTRING(e.OPER_STAT_DT,3,6) as operStatDt, SUBSTRING(e.OPER_END_DT,3,6) as operEndDt, " +
            "e.CTGY_ID as ctgyId, e.EVNT_TYPE_CD as eventTypeCd , IFNULL(eld.LIKE_YN,'N') AS LIKE_YN, " +
            "efd.FILE_URL as imageUrl, efd.FILE_THUB_URL as smALLImageUrl " +
            "from TB_EVNT_M e " +
            "LEFT OUTER JOIN TB_EVNT_VIEW_D evd ON e.EVNT_ID = evd.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_LIKE_D eld ON e.EVNT_ID = eld.EVNT_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_M efm ON e.TUMB_FILE_ID = efm.FILE_MST_ID " +
            "LEFT OUTER JOIN TB_EVNT_FILE_D efd ON efm.FILE_MST_ID = efd.FILE_MST_ID " +
            "WHERE e.DELT_YN = 'N' " +
            "ORDER BY CASE " +
            "WHEN :sortType = '10' THEN evd.VIEW_NMVL " +
            "WHEN :sortType = '20' THEN e.OPER_STAT_DT " +
            "WHEN :sortType = '30' THEN e.OPER_END_DT " +
            "ELSE e.OPER_STAT_DT END"
            , nativeQuery = true)
    List<Object[]> getEvntList(@Param("sortType") String sortType);

}
