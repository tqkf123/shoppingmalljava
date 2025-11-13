package com.example.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter // JSON -> 객체 변환
public class OrderRequestDto {
    // (임시) 나중에 Spring Security로 인증을 구현하면
    // 이 userId는 토큰에서 자동으로 가져오게 됩니다.
    // 지금은 테스트를 위해 클라이언트가 직접 ID를 보내는 방식(1번 회원)을 사용합니다.

    private List<OrderItemRequestDto> orderItems; // 주문할 상품 목록
}