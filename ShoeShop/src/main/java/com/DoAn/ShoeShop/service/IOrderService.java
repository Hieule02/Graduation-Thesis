package com.DoAn.ShoeShop.service;

import com.DoAn.ShoeShop.dto.respone.OrderResponse;
import com.DoAn.ShoeShop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderService {
    List<OrderResponse> findAll();
    Page<OrderResponse> findAll(Specification<Order> specification, Pageable pageable);

    List<OrderResponse> findByUserId(Long id);

    Order createOrder(Long id, String address, String paymentMethod);

    OrderResponse findById(Long id);

    void confirmOrder(Long id);

    int countOrders();

    Page<OrderResponse> findUnconfirmedOrders(Specification<Order> specification, Pageable pageable);

    Long getThisMonthIncome();

    void receive(Long id);

    void cancel(Long id);
}
