package com.snowroad.search.util;

import com.snowroad.config.auth.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static CustomUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        return (CustomUserDetails) auth.getPrincipal();
    }

    public static Long getCurrentUserId() {
        CustomUserDetails user = getCurrentUser();
        return (user != null) ? user.getUserId() : null;
    }
}
