package com.example.shoppingmall.config;

import com.example.shoppingmall.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Spring Security가 사용자를 인증할 때 사용할 User 객체
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    // --- UserDetails 인터페이스 메소드 구현 ---

    @Override
    public String getPassword() {
        return user.getPassword(); // 사용자의 암호화된 비밀번호
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // (중요) Spring Security에서 'username'은 ID를 의미 (여기선 email)
    }

    // (참고) 일단 모든 권한은 'true'로 설정
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // (참고) 권한(Role) 관리. 지금은 단순화를 위해 빈 리스트 반환
        // 예: return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }
}