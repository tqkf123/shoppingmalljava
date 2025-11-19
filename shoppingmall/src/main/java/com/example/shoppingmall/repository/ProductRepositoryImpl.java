package com.example.shoppingmall.repository;

import com.example.shoppingmall.domain.Product;
import com.example.shoppingmall.domain.QProduct;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        QProduct product = QProduct.product;

        QueryResults<Product> results = queryFactory
                .selectFrom(product)
                .where(
                        searchKeyword(keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.id.desc())
                .fetchResults();

        List<Product> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return QProduct.product.name.containsIgnoreCase(keyword)
                .or(QProduct.product.description.containsIgnoreCase(keyword));
    }
}