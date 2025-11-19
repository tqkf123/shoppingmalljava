package com.example.shoppingmall.controller;

import com.example.shoppingmall.config.CustomUserDetails;
import com.example.shoppingmall.dto.CartItemRequestDto;
import com.example.shoppingmall.dto.CartResponseDto;
import com.example.shoppingmall.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.shoppingmall.dto.CartItemUpdateDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart") // 공통 경로: /api/cart
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니에 상품 추가 API
     * POST /api/cart
     */
    @PostMapping
    public ResponseEntity<CartResponseDto> addItemToCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CartItemRequestDto requestDto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userEmail = userDetails.getUsername();
            CartResponseDto cartResponseDto = cartService.addItemToCart(userEmail, requestDto);

            // 성공 시: 200 OK와 최신 장바구니 상태 반환
            return ResponseEntity.ok(cartResponseDto);

        } catch (IllegalArgumentException e) {
            // (예: 상품 ID 없음, 장바구니 없음)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 내 장바구니 조회 API
     * GET /api/cart
     */
    @GetMapping
    public ResponseEntity<CartResponseDto> getMyCart(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userEmail = userDetails.getUsername();
            CartResponseDto cartResponseDto = cartService.getMyCart(userEmail);

            // 성공 시: 200 OK와 장바구니 정보 반환
            return ResponseEntity.ok(cartResponseDto);

        } catch (IllegalArgumentException e) {
            // (예: 장바구니 없음)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    // --- [ 1. ★추가★: 장바구니 아이템 삭제 API ] ---
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponseDto> deleteItemFromCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cartItemId
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userEmail = userDetails.getUsername();
            CartResponseDto updatedCart = cartService.deleteItemFromCart(userEmail, cartItemId);
            return ResponseEntity.ok(updatedCart); // 최신 장바구니 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // --- [ 2. ★추가★: 장바구니 아이템 수량 수정 API ] ---
    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponseDto> updateItemCount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long cartItemId,
            @RequestBody CartItemUpdateDto updateDto
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userEmail = userDetails.getUsername();
            CartResponseDto updatedCart = cartService.updateItemCount(userEmail, cartItemId, updateDto.getCount());
            return ResponseEntity.ok(updatedCart); // 최신 장바구니 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            // (재고 부족 또는 권한 없음)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // (메시지를 body에 담아 보내도 됨)
        }
    }

    // (참고) 장바구니 아이템 삭제 API(DELETE /api/cart/items/{cartItemId}) 등도
    // CartService와 여기에 추가할 수 있습니다.
}