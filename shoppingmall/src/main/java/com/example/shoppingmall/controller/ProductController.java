package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ProductCreateRequestDto;
import com.example.shoppingmall.dto.ProductResponseDto;
import com.example.shoppingmall.dto.ProductUpdateRequestDto;
import com.example.shoppingmall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; // [추가]
import org.springframework.data.domain.Pageable; // [추가]
import org.springframework.data.domain.Sort; // [추가]
import org.springframework.data.web.PageableDefault; // [추가]
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 이미지 업로드 경로
    private final String UPLOAD_DIR = "C:/shoppingmall-images/";

    /**
     * 상품 등록 API
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestPart("productDto") ProductCreateRequestDto requestDto,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        // 1. 이미지 저장 로직
        String imageUrl = saveImage(imageFile);

        try {
            // 2. 서비스 호출 (DTO와 이미지 URL 전달)
            ProductResponseDto responseDto = productService.createProduct(requestDto, imageUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * [수정됨] 상품 목록 조회 API (검색 & 페이징 적용)
     * GET /api/products?keyword=...&page=0&size=10
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // @PageableDefault: 파라미터가 없을 때 기본값 (10개씩, ID 역순)
        Page<ProductResponseDto> result = productService.getProducts(keyword, pageable);
        return ResponseEntity.ok(result);
    }

    /**
     * 단일 상품 상세 조회 API
     * GET /api/products/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        try {
            ProductResponseDto responseDto = productService.getProductById(productId);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 상품 수정 API
     * PUT /api/products/{productId}
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long productId,
            @RequestPart("productDto") ProductUpdateRequestDto requestDto,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        // 1. 새 이미지 저장 (이미지가 제공된 경우에만)
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = saveImage(imageFile);
        }

        try {
            // 2. 서비스 호출
            ProductResponseDto responseDto = productService.updateProduct(productId, requestDto, imageUrl);
            return ResponseEntity.ok(responseDto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 상품 삭제 API
     * DELETE /api/products/{productId}
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok("상품(ID: " + productId + ")이 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // --- 이미지 저장 헬퍼 메소드 ---
    private String saveImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            return null; // 이미지가 없으면 null 반환
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);

            // 업로드 디렉토리가 없으면 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 고유한 파일명 생성
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // 파일 저장
            Path filePath = uploadPath.resolve(uniqueFileName);
            imageFile.transferTo(filePath.toFile());

            // 클라이언트가 접근할 URL 반환
            return "/images/" + uniqueFileName;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("이미지 저장에 실패했습니다.", e);
        }
    }
}