package com.example.shoppingmall.dto;

import com.example.shoppingmall.domain.CartItem;
import lombok.Getter;

@Getter
public class CartItemResponseDto {

    private Long cartItemId; // [★핵심★] 삭제/수정을 위한 CartItem의 고유 ID
    private Long productId;
    private String productName;
    private int price; // 현재 상품 가격
    private int count;

    // CartItem 엔티티를 DTO로 변환하는 생성자
    public CartItemResponseDto(CartItem cartItem) {
        this.cartItemId = cartItem.getId(); // cartItem ID
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.price = cartItem.getProduct().getPrice(); // 주문과 달리 '현재가'
        this.count = cartItem.getCount();
    }
}