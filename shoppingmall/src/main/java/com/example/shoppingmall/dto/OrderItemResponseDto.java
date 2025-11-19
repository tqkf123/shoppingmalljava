package com.example.shoppingmall.dto;

import com.example.shoppingmall.domain.OrderItem;
import com.example.shoppingmall.domain.Product; // [추가]
import lombok.Getter;

@Getter
public class OrderItemResponseDto {
    // ( ... 기존 필드 ... )
    private Long productId;
    private String productName;
    private int orderPrice; // (참고) 장바구니에서는 '현재 상품 가격'
    private int count;

    // 1. (기존) OrderItem -> DTO
    public OrderItemResponseDto(OrderItem orderItem) {
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getName();
        this.orderPrice = orderItem.getOrderPrice(); // 주문 당시 가격
        this.count = orderItem.getCount();
    }

    // --- [ 2. (★추가★) CartItem -> DTO ] ---
    // CartItem은 '주문 가격'이 없으므로 Product의 '현재 가격'을 사용
    public OrderItemResponseDto(Product product, int count) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.orderPrice = product.getPrice(); // 현재 상품 가격
        this.count = count;
    }
}