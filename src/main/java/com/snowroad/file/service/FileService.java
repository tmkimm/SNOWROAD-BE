package com.snowroad.file.service;

import com.snowroad.event.domain.Events;
import com.snowroad.event.domain.EventsRepository;
import com.snowroad.event.web.dto.EventsSaveRequestDto;
import com.snowroad.file.domain.eventFilesDtl.EventFilesDtl;
import com.snowroad.file.domain.eventFilesDtl.EventFilesDtlRepository;
import com.snowroad.file.domain.eventFilesMst.EventFilesMst;
import com.snowroad.file.domain.eventFilesMst.EventFilesMstRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@NoArgsConstructor
@Service
public class FileService {
    @Autowired
    private EventFilesMstRepository filesMstRepository;

    @Autowired
    private EventFilesDtlRepository filesDtlRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private EventsRepository eventsRepository;

    private String filePath = "event-images";

    @Transactional
    public void uploadAllFiles(Long eventId, MultipartFile[] files, MultipartFile mainImage) throws IOException {
        // 이미지 검색
        Events event = eventsRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        // 썸네일 이미지 업로드 처리
        if (mainImage != null) {
            EventFilesMst tumbFileMst = this.saveEventFilesMst();

            // 파일의 원본 이름을 가져옴
            String originalFileName = mainImage.getOriginalFilename();
            // 파일 확장자 추출
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            // 고유한 UUID로 파일 이름 생성
            String uniqueFileName = FileNameGenerator.generateUniqueFileName() + fileExtension;

            String mainImagePath = s3Service.upload(mainImage, filePath, uniqueFileName);
            this.saveEventFilesDtl(tumbFileMst, filePath, mainImagePath, mainImage, uniqueFileName);

            // Event 객체를 가져와서 FILE_MST_ID를 설정
            event.updateTumbFile(tumbFileMst); // 파일 마스터 ID를 Events 엔티티에 설정
        }


        // 추가 파일들 업로드 처리
        EventFilesMst eventFileMst = this.saveEventFilesMst();
        for (MultipartFile file : files) {
            // 파일의 원본 이름을 가져옴
            String originalFileName = mainImage.getOriginalFilename();
            // 파일 확장자 추출
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            // 고유한 UUID로 파일 이름 생성
            String uniqueFileName = FileNameGenerator.generateUniqueFileName() + fileExtension;
            String filePathForAdditional = s3Service.upload(file, filePath, uniqueFileName);
            this.saveEventFilesDtl(eventFileMst, filePath, filePathForAdditional, file, uniqueFileName);
        }
        event.updateEventFile(eventFileMst); // 파일 마스터 ID를 Events 엔티티에 설정
        eventsRepository.save(event); // Event 엔티티를 업데이트하여 FILE_MST_ID를 저장


    }

    @Transactional
    public Long updateFileDetail(Long id, MultipartFile image) throws IOException  {
        EventFilesDtl file = filesDtlRepository.findById(id)
                .orElseThrow(() -> new
                        IllegalArgumentException("파일이 존재하지 않습니다. id" + id));

        // 파일의 원본 이름을 가져옴
        String originalFileName = image.getOriginalFilename();
        // 파일 확장자 추출
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 고유한 UUID로 파일 이름 생성
        String uniqueFileName = FileNameGenerator.generateUniqueFileName() + fileExtension;

        String mainImagePath = s3Service.update(image, filePath, uniqueFileName, filePath + "/" +file.getFileNm());

        file.update(filePath, uniqueFileName, image.getOriginalFilename(), null, image.getSize(), image.getContentType(), mainImagePath);
        return id;
    }

    public EventFilesMst saveEventFilesMst() {
        EventFilesMst fileMst = EventFilesMst.builder()
                .build();
        return filesMstRepository.save(fileMst);
    }

    public EventFilesDtl saveEventFilesDtl(EventFilesMst fileMst, String filePath, String fileUrl, MultipartFile file, String fileName) {
        EventFilesDtl fileDtl = EventFilesDtl.builder()
                .fileMst(fileMst)
                .filePath(filePath)
                .fileNm(fileName)
                .origFileNm(file.getOriginalFilename())
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .fileUrl(fileUrl)
                .build();
        return filesDtlRepository.save(fileDtl);
    }

    @Transactional
    public void deleteFileDetail(Long id) {
        EventFilesDtl file = filesDtlRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("파일이 존재하지 않습니다.. id="+id));

        s3Service.deleteFile(filePath + "/" + file.getFileNm());
        filesDtlRepository.delete(file);
    }


}
