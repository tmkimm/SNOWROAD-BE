package com.snowroad.common.web;

import com.snowroad.common.util.CurrentUser;
import com.snowroad.config.auth.LoginUser;
import com.snowroad.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController {
        @GetMapping("/")
        public String index(Model model, @LoginUser SessionUser user) {
                if(user != null) {
                        model.addAttribute("userName", user.getName());
                }
                return "index";
        }

        @GetMapping("/user")
        public ResponseEntity<?> getUserInfo(@CurrentUser UserDetails userDetails) {
                if (userDetails != null) {
                        // 유저 정보가 있을 경우
                        String username = userDetails.getUsername();
                        return ResponseEntity.ok("Authenticated user: " + username);
                } else {
                        // 유저 정보가 없을 경우
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
                }
        }

        @GetMapping("/upload/{eventId}")
        public String uploadForm(@PathVariable Long eventId, Model model) {
                model.addAttribute("eventId", eventId);
                return "upload"; // "upload.mustache" 템플릿을 반환
        }
}
