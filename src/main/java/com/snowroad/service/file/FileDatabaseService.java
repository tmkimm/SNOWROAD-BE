package com.snowroad.service.file;

import com.snowroad.domain.eventFilesDtl.EventFilesDtl;
import com.snowroad.domain.eventFilesDtl.EventFilesDtlRepository;
import com.snowroad.domain.eventFilesMst.EventFilesMst;
import com.snowroad.domain.eventFilesMst.EventFilesMstRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Service
public class FileDatabaseService {
    @Autowired
    private EventFilesMstRepository filesMstRepository;

    @Autowired
    private EventFilesDtlRepository filesDtlRepository;

    public EventFilesMst saveEventFilesMst(String filePath) {
        EventFilesMst fileMst = EventFilesMst.builder()
                .filePath(filePath)
                .build();
        return filesMstRepository.save(fileMst);
    }

    public EventFilesDtl saveEventFilesDtl(EventFilesMst fileMst, String filePath, MultipartFile file) {
        EventFilesDtl fileDtl = EventFilesDtl.builder()
                .fileMst(fileMst)
                .filePath(filePath)
                .fileNm(file.getOriginalFilename())
                .origFileNm(file.getOriginalFilename())
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .fileUrl(filePath)
                .build();
        return filesDtlRepository.save(fileDtl);
    }
}
