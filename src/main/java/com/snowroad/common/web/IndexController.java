package com.snowroad.common.web;

import com.snowroad.config.auth.LoginUser;
import com.snowroad.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {
        private final HttpSession httpSession;
        @GetMapping("/")
        public String index(Model model, @LoginUser SessionUser user) {
                if(user != null) {
                        model.addAttribute("userName", user.getName());
                }
                return "index";
        }

        @GetMapping("/user")
        public ResponseEntity<?> getUserInfo() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication != null && authentication.isAuthenticated()) {
                        // 인증된 사용자가 있을 경우
                        String username = authentication.getName();  // 유저 이름 가져오기
                        // 추가적인 유저 정보 처리 (예: userDetails.getUsername() 등을 활용)

                        return ResponseEntity.ok("Authenticated user: " + username);
                } else {
                        // 인증되지 않은 사용자 처리
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
                }
        }

        @GetMapping("/upload/{eventId}")
        public String uploadForm(@PathVariable Long eventId, Model model) {
                model.addAttribute("eventId", eventId);
                return "upload"; // "upload.mustache" 템플릿을 반환
        }
}
