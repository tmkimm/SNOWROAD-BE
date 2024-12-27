package com.snowroad.web;

import com.snowroad.config.auth.LoginUser;
import com.snowroad.config.auth.dto.SessionUser;
import com.snowroad.web.dto.EventsSaveRequestDto;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
