package com.snowroad.service;

import com.snowroad.domain.eventFilesMst.EventFilesMst;
import com.snowroad.domain.events.Events;
import com.snowroad.domain.events.EventsRepository;
import com.snowroad.service.file.FileDatabaseService;
import com.snowroad.service.file.FileLocalUploader;
import com.snowroad.service.file.FileS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FileUploadService {        // TODO 리팩토링 필요(전략패턴 적용, 데이터 생성 부분 분리)

    @Autowired
    private FileDatabaseService fileDatabaseService;
    @Autowired
    private FileS3Uploader fileS3Uploader;
    @Autowired
    private EventsRepository eventsRepository;

    private String filePath = "event-images";


    @Transactional
    public void uploadFiles(Long eventId, MultipartFile[] files, MultipartFile mainImage) throws IOException {
        String filePath = fileS3Uploader.upload(files[0], "event-images");  // 첫 번째 파일을 대표 파일로 가정
        EventFilesMst fileMst = fileDatabaseService.saveEventFilesMst(filePath);

        // 대표 이미지 업로드 처리
        if (mainImage != null) {
            String mainImagePath = fileS3Uploader.upload(mainImage, filePath);
            fileDatabaseService.saveEventFilesDtl(fileMst, filePath, mainImagePath, mainImage);
        }

        // 추가 파일들 업로드 처리
        for (MultipartFile file : files) {
            String filePathForAdditional = fileS3Uploader.upload(file, filePath);
            fileDatabaseService.saveEventFilesDtl(fileMst, filePath, filePathForAdditional, file);
        }

        // Event 객체를 가져와서 FILE_MST_ID를 설정
        Events event = eventsRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        event.updateFileMst(fileMst); // 파일 마스터 ID를 Events 엔티티에 설정
        eventsRepository.save(event); // Event 엔티티를 업데이트하여 FILE_MST_ID를 저장
    }

}