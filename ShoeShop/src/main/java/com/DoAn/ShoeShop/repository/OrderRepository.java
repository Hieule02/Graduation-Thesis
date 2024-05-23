package com.DoAn.ShoeShop.repository;

import com.DoAn.ShoeShop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findOrdersByUserIdOrderByCreateAtDesc(Long userId);
    @Query(value = "select count(1) from Order o")
    int countOrders();
    @Query(value = "select sum(o.productAmount + o.shippingFee) " +
            "from Order o where month(o.createAt) = month(curdate()) " +
            "and o.status = 'Đã nhận hàng'")
    Long getThisMonthIncome();
}
