package com.example.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // (주의) setOrder() 메소드 때문에 Setter 추가

@Entity
@Getter
@Setter // (중요) Order의 addOrderItem() 메소드에서 setOrder()를 사용하기 위해 추가
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    // (중요) '어떤 주문'에 속하는지 (Order와 연관관계)
    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계. 주문상품(N) : 주문(1)
    @JoinColumn(name = "order_id", nullable = false) // 외래 키(FK)
    private Order order;

    // (중요) '어떤 상품'인지 (Product와 연관관계)
    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계. 주문상품(N) : 상품(1)
    @JoinColumn(name = "product_id", nullable = false) // 외래 키(FK)
    private Product product;

    private int orderPrice; // 주문 당시 가격 (상품 가격은 변할 수 있으므로)
    private int count;      // 주문 수량

    // == 생성 메소드 == //
    public static OrderItem createOrderItem(Product product, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.product = product;
        orderItem.orderPrice = orderPrice;
        orderItem.count = count;

        // (중요) 주문 생성 시, 상품의 재고를 감소시킵니다.
        product.removeStock(count);
        return orderItem;
    }

    // == 비즈니스 로직 == //
    /**
     * 주문 취소 시 재고 수량 원복
     */
    public void cancel() {
        getProduct().addStock(count);
    }
}