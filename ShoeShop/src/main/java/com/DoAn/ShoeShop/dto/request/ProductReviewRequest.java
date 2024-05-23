package com.DoAn.ShoeShop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequest {
    private int rating;
    private String review;
    private Long productId;
}
