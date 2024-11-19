package com.snowroad.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class EventsFileUploadResponseDTO {

    @Schema(description = "파일 업로드 성공 메시지")
    private String message;

    @Schema(description = "업로드된 파일들의 정보", required = true)
    private List<FileInfo> files;

    // 생성자, getters, setters
    public EventsFileUploadResponseDTO(String message, List<FileInfo> files) {
        this.message = message;
        this.files = files;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    // 내부 클래스: 파일 정보
    public static class FileInfo {

        @Schema(description = "파일 이름")
        private String filename;

        @Schema(description = "파일 URL", example = "http://example.com/files/12345")
        private String fileUrl;

        @Schema(description = "파일 크기 (bytes)")
        private long size;

        public FileInfo(String filename, String fileUrl, long size) {
            this.filename = filename;
            this.fileUrl = fileUrl;
            this.size = size;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}
