package com.snowroad.entity;

import com.snowroad.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_EVNT_FILE_M")
public class EventFilesMst extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_MST_ID")
    private Long fileMstId;

    @OneToMany(mappedBy = "fileMst", fetch = FetchType.LAZY)
    private final List<EventFilesDtl> eventFilesDtlList = new ArrayList<>();

    @Builder
    public EventFilesMst(Long fileMstId) {
        this.fileMstId = fileMstId;
    }
}
