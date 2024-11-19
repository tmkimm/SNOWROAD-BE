package com.snowroad.web.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
@NoArgsConstructor
@Schema(description = "이벤트 파일 업로드 DTO")
public class EventsFileUploadRequestDTO {

    @Schema(description = "업로드할 파일들", required = true)
    private MultipartFile[] files;

    @Schema(description = "대표 이미지 (선택 사항)", required = true)
    private MultipartFile mainImage;

    // Getters and Setters
    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    public MultipartFile getMainImage() {
        return mainImage;
    }

    public void setMainImage(MultipartFile mainImage) {
        this.mainImage = mainImage;
    }
}