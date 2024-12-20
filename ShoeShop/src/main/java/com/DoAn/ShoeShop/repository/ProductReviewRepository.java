package com.DoAn.ShoeShop.repository;

import com.DoAn.ShoeShop.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findProductReviewsByProductId(Long productId);
}
