package com.snowroad.socialLogin.web;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "소셜 로그인 API", description = "로그인 시 사용하는 API")
@RestController
public class SocialLoginController {

    @Operation(
            summary = "구글 소셜 로그인",
            description = "구글을 통해 사용자가 로그인합니다. 로그인 후 성공적인 인증을 받으면 사용자 정보를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "인증 실패"
                    )
            }
    )
    @GetMapping("/oauth2/authorization/google")
    public void googleLogin() {
    }

    @Operation(
            summary = "네이버 소셜 로그인",
            description = "네이버를 통해 사용자가 로그인합니다. 로그인 후 성공적인 인증을 받으면 사용자 정보를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "인증 실패"
                    )
            }
    )
    @GetMapping("/oauth2/authorization/naver")
    public void naverLogin() {
    }

    @Operation(
            summary = "카카오 소셜 로그인",
            description = "카카오는 사용자가 로그인하는 API입니다. 성공적인 인증 후 사용자 정보를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "인증 실패"
                    )
            }
    )
    @GetMapping("/oauth2/authorization/kakao")
    public void kakaoLogin() {
    }
}
