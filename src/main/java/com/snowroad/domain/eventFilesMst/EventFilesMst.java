package com.snowroad.domain.eventFilesMst;

import com.snowroad.domain.BaseTimeEntity;
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

    @Column(name = "FILE_PATH")
    private String filePath;

    @Builder
    public EventFilesMst(Long fileMstId, String filePath) {
        this.fileMstId = fileMstId;
        this.filePath = filePath;
    }
}
