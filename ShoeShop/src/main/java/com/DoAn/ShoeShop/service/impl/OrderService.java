package com.DoAn.ShoeShop.service.impl;

import com.DoAn.ShoeShop.dto.respone.OrderResponse;
import com.DoAn.ShoeShop.entity.*;
import com.DoAn.ShoeShop.repository.CartRepository;
import com.DoAn.ShoeShop.repository.OrderRepository;
import com.DoAn.ShoeShop.repository.ProductRepository;
import com.DoAn.ShoeShop.repository.UserRepository;
import com.DoAn.ShoeShop.service.IOrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderService implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<OrderResponse> findAll() {
        return modelMapper.map(orderRepository.findAll(), new TypeToken<List<OrderResponse>>(){}.getType());
    }

    @Override
    public Page<OrderResponse> findAll(Specification<Order> specification, Pageable pageable) {
        return orderRepository.findAll(specification, pageable).map(entity ->
                modelMapper.map(entity, OrderResponse.class));
    }

    @Override
    public List<OrderResponse> findByUserId(Long id) {
        return modelMapper.map(orderRepository.findOrdersByUserIdOrderByCreateAtDesc(id), new TypeToken<List<OrderResponse>>(){}.getType());
    }

    @Override
    public Order createOrder(Long id, String address, String paymentMethod) {
        Order order = new Order();
        User user = userRepository.findById(id).get();

        order.setUser(user);
        order.setReceiverName(user.getUsername());
        order.setReceiverAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("Chờ xác nhận");
        order.setShippingFee(30000l);
        order.setProductAmount(cartRepository.getCartTotal(id));
        order.setReceiverPhone(user.getPhone());
        return orderRepository.save(order);
    }

    @Override
    public OrderResponse findById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()){
            return null;
        }
        return modelMapper.map(order.get(), OrderResponse.class);
    }

    @Override
    public void confirmOrder(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Đang giao hàng");
        for (OrderDetail item : order.getOrderDetails()){
            Product product = productRepository.findById(item.getProduct().getId()).get();
            product.setSoldQuantity(product.getSoldQuantity() + item.getQuantity());
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        orderRepository.save(order);
    }

    @Override
    public int countOrders() {
        return orderRepository.countOrders();
    }

    @Override
    public Page<OrderResponse> findUnconfirmedOrders(Specification<Order> specification, Pageable pageable) {
        return orderRepository.findAll(specification, pageable).map(entity ->
                modelMapper.map(entity, OrderResponse.class));
    }

    @Override
    public Long getThisMonthIncome() {
        return orderRepository.getThisMonthIncome();
    }

    @Override
    public void receive(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Đã nhận hàng");
        orderRepository.save(order);
    }

    @Override
    public void cancel(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus("Đã hủy");
        orderRepository.save(order);
    }
}
