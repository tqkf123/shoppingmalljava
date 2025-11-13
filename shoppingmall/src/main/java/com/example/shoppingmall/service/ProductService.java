package com.example.shoppingmall.service;

import com.example.shoppingmall.domain.Product;
import com.example.shoppingmall.dto.ProductCreateRequestDto;
import com.example.shoppingmall.dto.ProductResponseDto;
import com.example.shoppingmall.dto.ProductUpdateRequestDto; // [추가]
import com.example.shoppingmall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 등록 (기존 코드 ... 생략)
     */
    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto requestDto) {
        // ... (기존 createProduct 코드)
        Product product = requestDto.toEntity();
        Product savedProduct = productRepository.save(product);
        return new ProductResponseDto(savedProduct);
    }

    /**
     * 모든 상품 조회 (기존 코드 ... 생략)
     */
    public List<ProductResponseDto> getAllProducts() {
        // ... (기존 getAllProducts 코드)
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 단일 상품 상세 조회 (기존 코드 ... 생략)
     */
    public ProductResponseDto getProductById(Long productId) {
        // ... (기존 getProductById 코드)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다. id=" + productId));
        return new ProductResponseDto(product);
    }

    // --- [ 1. '상품 수정' 메소드 추가 ] ---

    /**
     * 상품 수정
     */
    @Transactional // (중요) DB 변경(UPDATE)이 발생하므로 readOnly = false
    public ProductResponseDto updateProduct(Long productId, ProductUpdateRequestDto requestDto) {
        // 1. 수정할 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다. id=" + productId));

        // 2. (보안) [고도화] 나중에 상품 등록자와 수정자가 같은지 확인하는 로직 추가 가능
        //    if (!product.getSeller().getEmail().equals(userEmail)) {
        //        throw new IllegalStateException("상품을 수정할 권한이 없습니다.");
        //    }

        // 3. 엔티티 내부의 update 메소드 호출 (11단계에서 만듦)
        product.update(
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getStockQuantity(),
                requestDto.getDescription()
        );

        // 4. (참고) @Transactional 덕분에 'product' 엔티티의 변경이
        //    자동으로 감지(Dirty Checking)되어 UPDATE 쿼리가 실행됩니다.
        //    (save를 호출할 필요 없음)

        // 5. 변경된 엔티티를 DTO로 변환하여 반환
        return new ProductResponseDto(product);
    }


    // --- [ 2. '상품 삭제' 메소드 추가 ] ---

    /**
     * 상품 삭제
     */
    @Transactional // (중요) DB 변경(DELETE)이 발생하므로 readOnly = false
    public void deleteProduct(Long productId) {
        // 1. 삭제할 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다. id=" + productId));

        // 2. (보안) [고도화] 수정과 마찬가지로 삭제 권한 확인 로직 추가 가능

        // 3. JpaRepository의 delete 메소드 호출
        productRepository.delete(product);

        // (참고) deleteById(productId)를 사용하면 조회 없이 바로 삭제도 가능
        // productRepository.deleteById(productId);
    }
}