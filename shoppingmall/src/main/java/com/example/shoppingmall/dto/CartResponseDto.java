package com.example.shoppingmall.dto;

import com.example.shoppingmall.domain.Cart;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CartResponseDto {

    private Long cartId;
    private List<CartItemResponseDto> cartItems;

    // Cart 엔티티를 DTO로 변환
    public CartResponseDto(Cart cart) {
        this.cartId = cart.getId();

        // Cart의 CartItem 리스트(엔티티)를
        // OrderItemResponseDto 리스트(DTO)로 변환
        // (CartItemResponseDto를 따로 만들어도 되지만, OrderItemResponseDto와
        //  보여줄 필드(상품명, 가격, 수량)가 동일하므로 재사용합니다.)
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemResponseDto::new) // CartItemResponseDto 생성자 사용
                .collect(Collectors.toList());
    }
}