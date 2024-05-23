package com.DoAn.ShoeShop.service;

import com.DoAn.ShoeShop.entity.Cart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICartService {
    List<Cart> findByUserId(Long id);

    Cart save(Cart cart);

    void deleteByProductIdAndColorAndSize(Long productId, String color, String size);

    Long getCartTotal(Long id);

    Cart findExistCart(Long id, Long productId, String color, String size);

    void deleteByUserId(Long id);
}
