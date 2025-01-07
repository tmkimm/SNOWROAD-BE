package com.snowroad.event.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long> {

    @Query("SELECT p from Events p ORDER BY p.eventId DESC")
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
}
