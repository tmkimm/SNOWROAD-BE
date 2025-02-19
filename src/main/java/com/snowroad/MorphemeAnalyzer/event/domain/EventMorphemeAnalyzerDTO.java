package com.snowroad.MorphemeAnalyzer.event.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity(name="TB_EVNT_NOUN_MRAN_M")
@Data
@Getter
@Setter
public class EventMorphemeAnalyzerDTO {
    @Id
    @Column(name="EVNT_ID")
    Long evntId;

    @Column(name="MRAN_CNTN")
    String mranCntn;
}
