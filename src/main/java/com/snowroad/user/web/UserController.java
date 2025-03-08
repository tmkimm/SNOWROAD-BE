package com.snowroad.user.web;


import com.snowroad.auth.web.dto.SignUpRequestDto;
import com.snowroad.common.exception.ForbiddenException;
import com.snowroad.common.exception.UnauthorizedException;
import com.snowroad.common.util.CookieUtil;
import com.snowroad.common.util.CurrentUser;
import com.snowroad.config.auth.dto.CustomUserDetails;
import com.snowroad.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(name = "사용자 API", description = "사용자 관련 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final CookieUtil cookieUtil;

    private final UserService userService;

    @Operation(
            summary = "회원가입",
            description = "소셜 로그인 회원가입 되지 않은 사용자일 경우 회원 가입을 진행합니다."
    )
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDto request, @CurrentUser CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 존재하지 않습니다. 로그인이 필요합니다.");
        }
        if (!Objects.equals(request.getUserId(), userDetails.getUserId())) {
            throw new ForbiddenException("권한이 존재하지 않습니다.");
        }
        userService.signUp(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
    @Operation(
            summary = "회원탈퇴"
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId, @CurrentUser CustomUserDetails userDetails, HttpServletResponse response) {
        if (userDetails == null) {
            throw new UnauthorizedException("인증 정보가 존재하지 않습니다. 로그인이 필요합니다.");
        }
        if (!Objects.equals(userId, userDetails.getUserId())) {
            throw new ForbiddenException("권한이 존재하지 않습니다.");
        }
        userService.deleteUser(userId);

        // 3️⃣ 쿠키 삭제 (공통 코드)
        cookieUtil.clearCookies(response);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}