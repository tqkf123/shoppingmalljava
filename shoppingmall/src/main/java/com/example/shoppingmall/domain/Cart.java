package com.example.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // (중요) '누구'의 장바구니인지 (User와 1:1 관계 -> N:1로 매핑)
    @OneToOne(fetch = FetchType.LAZY) // (수정) 1:1로 다시 시도
    @JoinColumn(name = "user_id", nullable = false, unique = true) // (중요) unique = true
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // == 생성 메소드 == //
    public static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.user = user;
        return cart;
    }
}