package com.snowroad.search.aspect;

import com.snowroad.search.annotation.SearchIndexing;
import com.snowroad.search.dto.SearchRequestDTO;
import com.snowroad.search.entity.SearchLog;
import com.snowroad.search.repository.SearchLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SearchIndexingAspect {

    private final SearchLogRepository searchLogRepository;

    @AfterReturning(pointcut = "@annotation(searchIndexing)", returning = "result")
    public void afterSave(JoinPoint joinPoint, SearchIndexing searchIndexing, Object result) {
        try {
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            // 1. 인자에서 SearchRequestDTO 추출
            String keyword = null;
            for (Object arg : joinPoint.getArgs()) {
                if (arg instanceof SearchRequestDTO dto) {
                    keyword = dto.getKeyword();
                    break;
                }
            }

            // 2. 유효성 검사
            if (keyword == null || keyword.trim().length() < 2) {
                log.warn("[SearchIndexingAspect] 유효하지 않은 키워드로 검색 로그를 저장하지 않음: '{}'", keyword);
                return;
            }

            // 3. 검색 로그 저장
            SearchLog logEntity = SearchLog.builder()
                    .srchCntn(keyword)
                    .userIp(getClientIp(request))
                    .userAgentCntn(request.getHeader("User-Agent"))
                    .dataCrtnDttm(LocalDateTime.now())
                    .build();

            searchLogRepository.save(logEntity);

        } catch (Exception e) {
            log.error("[SearchIndexingAspect] 검색 로그 저장 실패", e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return (forwarded != null) ? forwarded.split(",")[0] : request.getRemoteAddr();
    }
}