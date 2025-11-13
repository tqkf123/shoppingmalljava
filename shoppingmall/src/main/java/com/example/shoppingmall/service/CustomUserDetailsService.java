package com.example.shoppingmall.service;

import org.springframework.transaction.annotation.Transactional; // <-- 이 줄을 추가하세요!
import com.example.shoppingmall.config.CustomUserDetails;
import com.example.shoppingmall.domain.User;
import com.example.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Spring Security가 'username'으로 (우리는 email) 사용자를 찾으라고 호출
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다: " + email));

        return new CustomUserDetails(user);
    }
}