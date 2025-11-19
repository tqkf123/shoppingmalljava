package com.example.shoppingmall.service;

import com.example.shoppingmall.domain.Product;
import com.example.shoppingmall.dto.ProductCreateRequestDto;
import com.example.shoppingmall.dto.ProductResponseDto;
import com.example.shoppingmall.dto.ProductUpdateRequestDto;
import com.example.shoppingmall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; // [추가]
import org.springframework.data.domain.Pageable; // [추가]
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 등록
     */
    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto requestDto, String imageUrl) {
        Product product = Product.builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .stockQuantity(requestDto.getStockQuantity())
                .description(requestDto.getDescription())
                .imageUrl(imageUrl)
                .build();

        productRepository.save(product);
        return ProductResponseDto.from(product);
    }

    /**
     * [수정됨] 상품 검색 및 페이징 조회 (기존 getAllProducts 대체)
     */
    public Page<ProductResponseDto> getProducts(String keyword, Pageable pageable) {
        // 1. QueryDSL 리포지토리 호출 (searchProducts는 ProductRepositoryCustom에 정의됨)
        Page<Product> products = productRepository.searchProducts(keyword, pageable);

        // 2. Entity -> DTO 변환 (Page의 map 기능 사용)
        return products.map(ProductResponseDto::from);
    }

    /**
     * 단일 상품 상세 조회
     */
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다. id=" + productId));
        return ProductResponseDto.from(product);
    }

    /**
     * 상품 수정
     */
    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductUpdateRequestDto requestDto, String newImageUrl) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다. id=" + productId));

        String finalImageUrl = (newImageUrl != null) ? newImageUrl : product.getImageUrl();

        product.update(
                requestDto.getName(),
                requestDto.getPrice(),
                requestDto.getStockQuantity(),
                requestDto.getDescription(),
                finalImageUrl
        );
        return ProductResponseDto.from(product);
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다. id=" + productId));
        productRepository.delete(product);
    }
}