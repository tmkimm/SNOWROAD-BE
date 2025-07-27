package com.snowroad.admin.web.dto;

public record EventSimpleListResponseDto(
        Long eventId,
        String eventNm,
        String eventCntn,
        String eventAddr,
        String rads,
        String lnad,
        String operStatDt,
        String operEndDt,
        String operDttmCntn,
        String ctgyId,
        String eventTypeCd,
        Double addrLttd,
        Double addrLotd,
        String deleteYn,
        String ldcd,
        String eventDetailUrl
) {}
