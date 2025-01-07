package com.snowroad.common.web;

import com.snowroad.config.auth.LoginUser;
import com.snowroad.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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

        @GetMapping("/upload/{eventId}")
        public String uploadForm(@PathVariable Long eventId, Model model) {
                model.addAttribute("eventId", eventId);
                return "upload"; // "upload.mustache" 템플릿을 반환
        }
}
