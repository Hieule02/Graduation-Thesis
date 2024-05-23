package com.DoAn.ShoeShop.service.impl;

import com.DoAn.ShoeShop.entity.Cart;
import com.DoAn.ShoeShop.entity.Order;
import com.DoAn.ShoeShop.entity.OrderDetail;
import com.DoAn.ShoeShop.repository.CartRepository;
import com.DoAn.ShoeShop.repository.OrderDetailRepository;
import com.DoAn.ShoeShop.repository.ProductRepository;
import com.DoAn.ShoeShop.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDetailService implements IOrderDetailService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public void saveOrderDetailList(Long userId, Order order) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for(Cart cart : carts){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductName(cart.getProductName());
            orderDetail.setImage(cart.getImage());
            orderDetail.setPrice(cart.getPrice());
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setTotal(cart.getTotal());
            orderDetail.setColor(cart.getColor());
            orderDetail.setSize(cart.getSize());
            orderDetail.setProduct(productRepository.findById(cart.getProductId()).get());
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long id) {
        return orderDetailRepository.findOrderDetailsByOrderId(id);
    }
}
