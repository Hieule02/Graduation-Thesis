package com.DoAn.ShoeShop.service;

import com.DoAn.ShoeShop.dto.request.ProductRequest;
import com.DoAn.ShoeShop.dto.request.ProductReviewRequest;
import com.DoAn.ShoeShop.dto.respone.ProductResponse;
import com.DoAn.ShoeShop.entity.Product;
import com.DoAn.ShoeShop.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductService {
    List<ProductResponse> getAll();

    Page<ProductResponse> getAll(Specification<Product> specification, Pageable pageable);

    List<ProductResponse> getNewProduct();

    List<ProductResponse> getProductsByCategory_Slug(Specification<Product> specification, Sort sort);
    Product createProduct(ProductRequest productRequest);

    ProductResponse findById(Long id);

    void update(ProductRequest productRequest);

    void deleteProduct(Long id);

    List<ProductResponse> getRandomProduct();

    int countProducts();

    void createReview(ProductReviewRequest productReviewRequest, Long userId);

    List<ProductReview> findReviewsByProductId(Long productId);

    List<ProductResponse> getSuggestProducts(Long id);
}
