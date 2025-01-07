package com.snowroad.file.domain.eventFilesMst;

import com.snowroad.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_EVNT_FILE_M")
public class EventFilesMst extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_MST_ID")
    private Long fileMstId;


    @Builder
    public EventFilesMst(Long fileMstId) {
        this.fileMstId = fileMstId;
    }
}
