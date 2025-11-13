package com.example.shoppingmall.service;

import com.example.shoppingmall.config.JwtTokenProvider;
import com.example.shoppingmall.domain.Cart; // [추가]
import com.example.shoppingmall.domain.User;
import com.example.shoppingmall.dto.UserLoginRequestDto;
import com.example.shoppingmall.dto.UserLoginResponseDto;
import com.example.shoppingmall.dto.UserSignUpRequestDto;
import com.example.shoppingmall.repository.CartRepository; // [추가]
import com.example.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
// @Transactional(readOnly = true) // <- signUp, login이 있으므로 클래스 레벨에서 삭제
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CartRepository cartRepository; // [추가]

    /**
     * 회원가입 비즈니스 로직
     */
    @Transactional
    public Long signUp(UserSignUpRequestDto requestDto) {

        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .build();

        // 1. User 저장
        User savedUser = userRepository.save(user);

        // --- [ 2. (★추가★) 빈 장바구니 생성 ] ---
        //    회원가입과 동시에 1:1로 매핑되는 빈 장바구니를 생성합니다.
        Cart cart = Cart.createCart(savedUser);
        cartRepository.save(cart);

        return savedUser.getId();
    }

    /**
     * 로그인 비즈니스 로직 (기존 코드 ... 생략)
     */
    @Transactional(readOnly = true)
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        // ... (기존 login 코드)
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String token = jwtTokenProvider.createToken(user.getEmail());
        return new UserLoginResponseDto(token);
    }
}