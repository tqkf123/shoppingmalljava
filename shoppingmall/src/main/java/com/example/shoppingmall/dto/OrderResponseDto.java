package com.example.shoppingmall.dto;

import com.example.shoppingmall.domain.Order;
import com.example.shoppingmall.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {

    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemResponseDto> orderItems; // 주문 상품 목록

    // Order 엔티티를 DTO로 변환하는 생성자
    public OrderResponseDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();

        // (중요) Order의 OrderItem 리스트(엔티티)를
        // OrderItemResponseDto 리스트(DTO)로 변환합니다.
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponseDto::new)
                .collect(Collectors.toList());
    }
}