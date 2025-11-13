package com.example.shoppingmall.service;

import com.example.shoppingmall.domain.Cart;
import com.example.shoppingmall.domain.CartItem;
import com.example.shoppingmall.domain.Product;
import com.example.shoppingmall.dto.CartItemRequestDto;
import com.example.shoppingmall.dto.CartResponseDto;
import com.example.shoppingmall.repository.CartItemRepository;
import com.example.shoppingmall.repository.CartRepository;
import com.example.shoppingmall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    /**
     * 장바구니에 상품 추가
     */
    @Transactional
    public CartResponseDto addItemToCart(String userEmail, CartItemRequestDto requestDto) {

        // 1. (중요) 인증된 이메일로 장바구니(Cart) 조회
        //    (47단계에서 회원가입 시 빈 Cart가 생성되므로, 항상 존재해야 함)
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

        // 2. 장바구니에 담을 상품(Product) 조회
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 3. (핵심) 해당 장바구니에 이 상품이 *이미* 담겨 있는지 확인
        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartIdAndProductId(
                cart.getId(),
                product.getId()
        );

        if (optionalCartItem.isPresent()) {
            // 4-1. (Case 1) 이미 담긴 상품 -> 수량만 더하기
            CartItem cartItem = optionalCartItem.get();
            cartItem.addCount(requestDto.getCount());
            // (참고) @Transactional 덕분에 'cartItem'의 변경이 자동 저장(Dirty Checking)
        } else {
            // 4-2. (Case 2) 처음 담는 상품 -> 새 CartItem 생성 및 저장
            CartItem cartItem = CartItem.createCartItem(
                    cart,
                    product,
                    requestDto.getCount()
            );
            cartItemRepository.save(cartItem);
        }

        // 5. 최신화된 장바구니 정보(DTO)를 반환
        //    (CartRepository에서 Cart를 다시 조회해야 최신 CartItem 목록 반영)
        Cart updatedCart = cartRepository.findById(cart.getId()).get();
        return new CartResponseDto(updatedCart);
    }

    /**
     * 내 장바구니 조회
     */
    @Transactional(readOnly = true)
    public CartResponseDto getMyCart(String userEmail) {
        // 1. (N+1 문제 발생 가능 지점)
        //    Cart를 찾고, DTO로 변환하는 과정에서 CartItem -> Product를
        //    지연 로딩(LAZY)으로 여러 번 조회할 수 있음.
        Cart cart = cartRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

        // 2. Entity -> DTO 변환
        return new CartResponseDto(cart);
    }

    // (참고) 장바구니 상품 삭제, 수량 변경 등의 로직도 여기에 추가
}