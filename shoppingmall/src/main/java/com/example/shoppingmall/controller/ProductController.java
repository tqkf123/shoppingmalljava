package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ProductCreateRequestDto;
import com.example.shoppingmall.dto.ProductResponseDto;
import com.example.shoppingmall.dto.ProductUpdateRequestDto; // [추가]
import com.example.shoppingmall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * 상품 등록 API (기존 코드 ... 생략)
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateRequestDto requestDto) {
        // ... (기존 createProduct 코드)
        ProductResponseDto responseDto = productService.createProduct(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    /**
     * 모든 상품 조회 API (기존 코드 ... 생략)
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        // ... (기존 getAllProducts 코드)
        List<ProductResponseDto> productList = productService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    /**
     * 단일 상품 상세 조회 API (기존 코드 ... 생략)
     * GET /api/products/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        // ... (기존 getProductById 코드)
        try {
            ProductResponseDto responseDto = productService.getProductById(productId);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // --- [ 1. '상품 수정' API 추가 ] ---

    /**
     * 상품 수정 API
     * PUT /api/products/{productId}
     * (참고: PUT은 전체 교체, PATCH는 부분 수정이지만, 여기서는 PUT으로 통용)
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequestDto requestDto
    ) {
        // [보안] 상품 수정 API도 인증(로그인)된 사용자만 호출할 수 있습니다.
        // (SecurityConfig에서 /api/products (POST)와 마찬가지로
        // PUT, DELETE도 자동으로 'authenticated'가 적용된 상태입니다.)

        try {
            ProductResponseDto responseDto = productService.updateProduct(productId, requestDto);

            // 성공 시: 200 OK와 수정된 상품 정보 반환
            return ResponseEntity.ok(responseDto);

        } catch (IllegalArgumentException e) {
            // 실패 시 (예: ID 없음): 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // --- [ 2. '상품 삭제' API 추가 ] ---

    /**
     * 상품 삭제 API
     * DELETE /api/products/{productId}
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        // [보안] 상품 삭제 API도 인증된 사용자만 호출 가능

        try {
            productService.deleteProduct(productId);

            // 성공 시: 200 OK와 성공 메시지 반환
            return ResponseEntity.ok("상품(ID: " + productId + ")이 성공적으로 삭제되었습니다.");

            // (참고) HTTP 204 No Content를 반환하여 body 없이 응답할 수도 있습니다.
            // return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            // 실패 시 (예: ID 없음): 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}