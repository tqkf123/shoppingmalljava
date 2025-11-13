package com.example.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // JSON -> 객체 변환
public class OrderItemRequestDto {
    private Long productId; // 주문할 상품 ID
    private int count;      // 주문할 수량
}