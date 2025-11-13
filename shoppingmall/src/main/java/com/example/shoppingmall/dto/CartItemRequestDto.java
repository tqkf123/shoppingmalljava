package com.example.shoppingmall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // JSON -> 객체 변환
public class CartItemRequestDto {
    private Long productId; // 담을 상품 ID
    private int count;      // 담을 수량
}