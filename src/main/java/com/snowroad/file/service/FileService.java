package com.snowroad.file.service;

import com.snowroad.common.exception.BadRequestException;
import com.snowroad.entity.Events;
import com.snowroad.event.domain.EventsRepository;
import com.snowroad.entity.EventFilesDtl;
import com.snowroad.file.domain.eventFilesDtl.EventFilesDtlRepository;
import com.snowroad.entity.EventFilesMst;
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


    // detail에 파일 추가
    @Transactional
    public void addFileDetail(Long id, MultipartFile[] images) throws IOException {
        // 이벤트 조회
        Events event = eventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        EventFilesMst detailFileMst = event.getEventFiles();
        if (detailFileMst == null) {
            detailFileMst = this.saveEventFilesMst();
            event.updateEventFile(detailFileMst);
        }

        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;

            // 고유한 UUID로 파일 이름 생성
            String uniqueFileName = FileNameGenerator.generateUniqueFileName(image);

            // S3 업로드
            String imagePath = s3Service.upload(image, filePath, uniqueFileName);

            // 파일 정보 저장
            this.saveEventFilesDtl(detailFileMst, filePath, imagePath, image, uniqueFileName);
        }
    }

    // 메인 파일 추가
    @Transactional
    public void addFileMain(Long id, MultipartFile image) throws IOException {
        // 이미지 검색
        Events event = eventsRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        EventFilesMst tumbFileMst = event.getEventTumbfile();
        if(tumbFileMst == null) {
            tumbFileMst = this.saveEventFilesMst();
            event.updateTumbFile(tumbFileMst);
        }

        boolean existsDtl = filesDtlRepository.existsByFileMst(tumbFileMst);
        if (existsDtl) {
            throw new BadRequestException("대표 파일은 이미 업로드되어 있습니다.");
        }

        // 고유한 UUID로 파일 이름 생성
        String uniqueFileName = FileNameGenerator.generateUniqueFileName(image);
        String imagePath = s3Service.upload(image, filePath, uniqueFileName);
        this.saveEventFilesDtl(tumbFileMst, filePath, imagePath, image, uniqueFileName);
    }
    @Transactional
    public Long updateFileDetail(Long id, MultipartFile image) throws IOException  {
        EventFilesDtl file = filesDtlRepository.findById(id)
                .orElseThrow(() -> new
                        IllegalArgumentException("파일이 존재하지 않습니다. id" + id));

        // 고유한 UUID로 파일 이름 생성
        String uniqueFileName = FileNameGenerator.generateUniqueFileName(image);

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
