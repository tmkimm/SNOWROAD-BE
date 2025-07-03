package com.snowroad.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.snowroad.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_EVNT_FILE_D")
public class EventFilesDtl extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_DTL_ID")
    private Long fileDtlId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_MST_ID")
    @JsonBackReference
    private EventFilesMst fileMst;

    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "FILE_NM")
    private String fileNm;

    @Column(name = "ORIG_FILE_NM")
    private String origFileNm;

    @Column(name = "FILE_THUB_URL")
    private String fileThumbUrl;

    @Column(name = "FILE_SIZE")
    private Long fileSize;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_URL")
    private String fileUrl;

    @Builder
    public EventFilesDtl(Long fileDtlId, EventFilesMst fileMst, String filePath, String fileNm, String origFileNm, String fileThumbUrl, Long fileSize, String fileType, String fileUrl) {
        this.fileDtlId = fileDtlId;
        this.fileMst = fileMst;
        this.filePath = filePath;
        this.fileNm = fileNm;
        this.origFileNm = origFileNm;
        this.fileThumbUrl = fileThumbUrl;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
    }

    public void update(String filePath, String fileNm, String origFileNm, String fileThumbUrl, Long fileSize, String fileType, String fileUrl) {
        this.filePath = filePath;
        this.fileNm = fileNm;
        this.origFileNm = origFileNm;
        this.fileThumbUrl = fileThumbUrl;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
    }
}
