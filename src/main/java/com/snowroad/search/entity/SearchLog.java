package com.snowroad.search.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_SRCH_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SRCH_LOG_ID")
    private Long srchLogId;

    @Column(name = "SRCH_CNTN", nullable = false)
    private String srchCntn;

    @Column(name = "MPAZ_CNTN")
    private String mpazCntn;

    @Column(name = "USER_IP")
    private String userIp;

    @Column(name = "USER_AGENT_CNTN")
    private String userAgentCntn;

    @Column(name = "DATA_CRTN_DTTM", nullable = false)
    private LocalDateTime dataCrtnDttm;

    @Column(name = "DATA_EDIT_DTTM")
    private LocalDateTime dataEditDttm;

    @Column(name = "DATA_DELT_DTTM")
    private LocalDateTime dataDeltDttm;
}
