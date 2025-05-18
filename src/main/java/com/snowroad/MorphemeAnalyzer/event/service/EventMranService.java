package com.snowroad.MorphemeAnalyzer.event.service;

import com.snowroad.MorphemeAnalyzer.event.Interface.EventMranInterface;
import com.snowroad.MorphemeAnalyzer.event.domain.EventNounMran;
import com.snowroad.MorphemeAnalyzer.event.repository.EventNounMranRepository;
import com.snowroad.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import com.snowroad.event.web.dto.EventsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventMranService implements EventMranInterface {

    private final EventNounMranRepository eventNounMranRepository;
    private final KomoranAnalyzerInterface komoranAnalyzerInterface;

    @Override
    public void saveEventMran(EventsSaveRequestDto requestDto) {
        String originalEventNm = requestDto.getEventNm();
        Map<String, List<KomoranDTO>> komoranAnalyzerMap = komoranAnalyzerInterface.komoranAnalyzerMap(originalEventNm);

        komoranAnalyzerMap.get("NNG").forEach(komoranDTO -> eventNounMranRepository.save(new EventNounMran(
                requestDto.toEntity().getEventId(),
                komoranDTO.getToken()
        )));
    }
}
