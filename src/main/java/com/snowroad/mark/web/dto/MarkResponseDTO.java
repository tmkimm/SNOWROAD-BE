package com.snowroad.mark.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MarkResponseDTO {
    private String eventId;

    private String likeYn;
}