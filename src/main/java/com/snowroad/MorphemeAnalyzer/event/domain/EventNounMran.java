package com.snowroad.MorphemeAnalyzer.event.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity(name="TB_EVNT_NOUN_MRAN_M")
@Data
@Getter
@Setter
@NoArgsConstructor
public class EventNounMran {

    @Id
    @Column(name="EVNT_ID")
    Long evntId;

    @Column(name="MRAN_CNTN")
    String mranCntn;

    @Column(name = "DATA_CRTN_DTTM", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    public EventNounMran(Long evntId, String mranCntn) {
        this.evntId = evntId;
        this.mranCntn = mranCntn;
        this.createdDate = LocalDateTime.now();
    }
}
