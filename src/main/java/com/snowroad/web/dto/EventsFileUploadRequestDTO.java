package com.snowroad.web.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
@NoArgsConstructor
@Schema(description = "이벤트 파일 업로드 DTO")
public class EventsFileUploadRequestDTO {

    @Schema(description = "내용 이미지(배열)", required = true)
    private MultipartFile[] contentImages;

    @Schema(description = "대표 이미지 ", required = true)
    private MultipartFile mainImage;

    // Getters and Setters
    public MultipartFile[] getFiles() {
        return contentImages;
    }

    public void setFiles(MultipartFile[] files) {
        this.contentImages = files;
    }

    public MultipartFile getMainImage() {
        return mainImage;
    }

    public void setMainImage(MultipartFile mainImage) {
        this.mainImage = mainImage;
    }
}