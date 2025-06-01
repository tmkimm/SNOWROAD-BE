package com.snowroad.MorphemeAnalyzer.aspect;

import com.snowroad.MorphemeAnalyzer.event.domain.EventNounMran;
import com.snowroad.MorphemeAnalyzer.event.repository.EventNounMranRepository;
import com.snowroad.MorphemeAnalyzer.komoran.domain.KomoranDTO;
import com.snowroad.MorphemeAnalyzer.komoran.interfaces.KomoranAnalyzerInterface;
import com.snowroad.entity.Events;
import com.snowroad.event.domain.EventsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MorphIndexingAspect {

    private final EventsRepository eventsRepository;
    private final EventNounMranRepository eventNounMranRepository;
    private final KomoranAnalyzerInterface komoranAnalyzerInterface;

    @AfterReturning(pointcut = "@annotation(com.snowroad.MorphemeAnalyzer.annotation.MorphIndexing)", returning = "result")
    public void afterSave(Object result) {
        try{
            if (result instanceof Long id) {
                Optional<Events> optional =  eventsRepository.findById(id);
                if (optional.isPresent()) {
                    Events event = optional.get();
                    String eventNm = event.getEventNm();
                    log.info("MorphIndexing: id = {}" , id);
                    log.info("eventNm = {}", eventNm);
                    Map<String, List<KomoranDTO>> komoranAnalyzerMap = komoranAnalyzerInterface.komoranAnalyzerMap(eventNm);
                    komoranAnalyzerMap.get("NNG").forEach(komoranDTO -> eventNounMranRepository.save(new EventNounMran(
                            id,
                            komoranDTO.getToken()
                    )));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}