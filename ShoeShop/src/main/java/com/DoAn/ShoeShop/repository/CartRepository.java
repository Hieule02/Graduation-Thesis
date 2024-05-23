package com.DoAn.ShoeShop.repository;

import com.DoAn.ShoeShop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long id);

    @Query(value = "select sum(c.total) from Cart c where c.userId = :userId")
    Long getCartTotal(Long userId);

    void deleteCartByProductIdAndColorAndSize(Long productId, String color, String size);

    void deleteCartsByUserId(Long userId);
    Cart findCartByUserIdAndProductIdAndColorAndSize(Long userId, Long productId, String color, String size);
}
