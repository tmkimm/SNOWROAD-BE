package com.snowroad.file.service;

import com.snowroad.file.domain.eventFilesMst.EventFilesMst;
import com.snowroad.event.domain.Events;
import com.snowroad.event.domain.EventsRepository;
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
        // 이미지 검색
        Events event = eventsRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        // 썸네일 이미지 업로드 처리
        if (mainImage != null) {
            EventFilesMst tumbFileMst = fileDatabaseService.saveEventFilesMst();
            String mainImagePath = fileS3Uploader.upload(mainImage, filePath);
            fileDatabaseService.saveEventFilesDtl(tumbFileMst, filePath, mainImagePath, mainImage);


            // Event 객체를 가져와서 FILE_MST_ID를 설정

            event.updateTumbFile(tumbFileMst); // 파일 마스터 ID를 Events 엔티티에 설정
        }


        // 추가 파일들 업로드 처리
        EventFilesMst eventFileMst = fileDatabaseService.saveEventFilesMst();
        for (MultipartFile file : files) {
            String filePathForAdditional = fileS3Uploader.upload(file, filePath);
            fileDatabaseService.saveEventFilesDtl(eventFileMst, filePath, filePathForAdditional, file);
        }
        event.updateEventFile(eventFileMst); // 파일 마스터 ID를 Events 엔티티에 설정
        eventsRepository.save(event); // Event 엔티티를 업데이트하여 FILE_MST_ID를 저장


    }

}
