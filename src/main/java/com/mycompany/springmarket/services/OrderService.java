package com.mycompany.springmarket.services;

import com.mycompany.springmarket.entity.OrderEntity;
import com.mycompany.springmarket.entity.UserEntity;
import com.mycompany.springmarket.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderEntity saveOrder(OrderEntity order) {
        order.setOrderDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public List<OrderEntity> getOrdersByUser(UserEntity user) {
        return orderRepository.findByUser(user);

    }
    public OrderEntity getOrder(UserEntity user) {
        return orderRepository.findByUserAndCancelledFalse(user);

    }

    public void cancelOrder(OrderEntity order) {
        order.setCancelled(true);
        orderRepository.save(order);
    }

    public OrderEntity getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public boolean doesUserOwnOrder(Long orderId, UserEntity user) {
        Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);
        return orderOptional.isPresent() && orderOptional.get().getUser().equals(user);
    }

}
