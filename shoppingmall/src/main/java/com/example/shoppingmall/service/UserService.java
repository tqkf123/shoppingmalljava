package com.example.shoppingmall.service;

import com.example.shoppingmall.config.JwtTokenProvider;
import com.example.shoppingmall.domain.Cart;
import com.example.shoppingmall.domain.User;
import com.example.shoppingmall.domain.UserRoleEnum; // [import 확인]
import com.example.shoppingmall.dto.UserLoginRequestDto;
import com.example.shoppingmall.dto.UserLoginResponseDto;
import com.example.shoppingmall.dto.UserSignUpRequestDto;
import com.example.shoppingmall.repository.CartRepository;
import com.example.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CartRepository cartRepository;

    @Transactional
    public Long signUp(UserSignUpRequestDto requestDto) {

        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // --- [ 1. 권한(Role) 결정 로직 ] ---
        UserRoleEnum role = UserRoleEnum.USER; // 기본값: 일반 유저

        // 관리자 이메일인 경우 ADMIN 권한 부여
        if (requestDto.getEmail().equals("admin@example.com")) {
            role = UserRoleEnum.ADMIN;
        }
        // --------------------------------

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // --- [ 2. User 생성 (여기에 .role(role)이 꼭 있어야 함!) ] ---
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .role(role) // [★핵심 수정★] 이 줄이 없어서 에러가 났던 겁니다!
                .build();

        User savedUser = userRepository.save(user);

        // 회원가입 시 빈 장바구니 생성
        Cart cart = Cart.createCart(savedUser);
        cartRepository.save(cart);

        return savedUser.getId();
    }

    @Transactional(readOnly = true)
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 생성 시 권한 정보도 함께 전달
        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole());

        return new UserLoginResponseDto(token);
    }
}