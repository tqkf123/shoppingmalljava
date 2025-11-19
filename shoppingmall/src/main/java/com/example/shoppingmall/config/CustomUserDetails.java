package com.example.shoppingmall.config;

import com.example.shoppingmall.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    // --- [ ★ 여기가 403 해결의 핵심입니다! ★ ] ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // DB에 저장된 Role (USER, ADMIN)을 가져와서 "ROLE_" 접두사를 붙여 권한을 만듭니다.
        // 예: "USER" -> "ROLE_USER"
        String roleName = "ROLE_" + user.getRole().name();

        // 권한 목록 반환 (이게 없으면 403 에러가 납니다)
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }
    // ----------------------------------------

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Spring Security에서 username은 email로 사용
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}