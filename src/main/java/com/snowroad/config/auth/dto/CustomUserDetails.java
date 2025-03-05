package com.snowroad.config.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final Long userId;
    private final String username;
    private final String role;
    private final boolean isRegistered;

    public CustomUserDetails(Long userId, String username, String role, boolean isRegistered) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.isRegistered = isRegistered;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 필요하면 권한을 추가
    }

    @Override
    public String getPassword() {
        return null; // JWT 기반이라 비밀번호 필요 없음
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}