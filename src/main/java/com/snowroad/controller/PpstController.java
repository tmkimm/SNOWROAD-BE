package com.snowroad.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "팝업스토어 API", description = "팝업 스토어 관련 API")
public class PpstController {
    @Operation(summary="팝업스토어 등록", description = "관리자 페이지에서 팝업스토어 정보를 등록합니다.")
    @PostMapping("/api/v1/posts")
    public void save() {
    }

//    @PostMapping("/api/v1/posts")
//    public Long save(@RequestBody PostsSaveRequestDto requestDto) {
//        return postsService.save(requestDto);
//    }
}
