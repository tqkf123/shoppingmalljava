package com.example.shoppingmall.controller;

import com.example.shoppingmall.config.CustomUserDetails;
import com.example.shoppingmall.dto.OrderRequestDto;
import com.example.shoppingmall.dto.OrderResponseDto; // [추가]
import com.example.shoppingmall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*; // [수정] web.bind.annotation.*

import java.util.List; // [추가]

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성 API (기존 코드 ... 생략)
     */
    @PostMapping
    public ResponseEntity<String> createOrder(
            @RequestBody OrderRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // ( ... 기존 createOrder 메소드 내용은 그대로 ... )
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("인증 정보가 없습니다.");
        }
        try {
            String userEmail = userDetails.getUsername();
            Long savedOrderId = orderService.createOrder(requestDto, userEmail);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("주문이 성공적으로 생성되었습니다. 주문 ID: " + savedOrderId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // --- [ 1. '내 주문 목록 조회' API 추가 ] ---

    /**
     * 내 주문 목록 조회 API
     * GET /api/orders/my
     */
    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userEmail = userDetails.getUsername();
        List<OrderResponseDto> myOrders = orderService.getMyOrders(userEmail);

        return ResponseEntity.ok(myOrders); // 200 OK
    }

    // --- [ 2. '주문 취소' API 추가 ] ---

    /**
     * 주문 취소 API
     * POST /api/orders/{orderId}/cancel
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userEmail = userDetails.getUsername();
            orderService.cancelOrder(orderId, userEmail);

            return ResponseEntity.ok("주문(ID: " + orderId + ")이 성공적으로 취소되었습니다.");

        } catch (IllegalArgumentException e) {
            // (예: 주문 ID 없음)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            // (예: 취소 권한 없음, 재고 오류 등)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}