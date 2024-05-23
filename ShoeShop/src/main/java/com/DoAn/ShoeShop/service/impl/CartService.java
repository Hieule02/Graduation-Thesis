package com.DoAn.ShoeShop.service.impl;

import com.DoAn.ShoeShop.entity.Cart;
import com.DoAn.ShoeShop.repository.CartRepository;
import com.DoAn.ShoeShop.service.ICartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartService implements ICartService {
    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Cart> findByUserId(Long id) {
        return cartRepository.findByUserId(id);
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteByProductIdAndColorAndSize(Long productId, String color, String size) {
        cartRepository.deleteCartByProductIdAndColorAndSize(productId, color, size);
    }

    @Override
    public Long getCartTotal(Long id) {
        return cartRepository.getCartTotal(id);
    }

    @Override
    public Cart findExistCart(Long userId, Long productId, String color, String size) {
        return cartRepository.findCartByUserIdAndProductIdAndColorAndSize(userId, productId, color, size);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long id) {
        cartRepository.deleteCartsByUserId(id);
    }

}
