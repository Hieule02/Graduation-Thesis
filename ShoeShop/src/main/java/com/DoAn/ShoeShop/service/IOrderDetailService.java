package com.DoAn.ShoeShop.service;

import com.DoAn.ShoeShop.entity.Order;
import com.DoAn.ShoeShop.entity.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderDetailService {
    void saveOrderDetailList(Long userId, Order order);

    List<OrderDetail> findByOrderId(Long id);
}
