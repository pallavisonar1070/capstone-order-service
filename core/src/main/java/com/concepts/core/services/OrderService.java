package com.concepts.core.services;

import com.concepts.core.dtos.CreateOrderRequest;
import com.concepts.core.models.Order;
import com.concepts.core.models.OrderStatus;
import com.concepts.core.repos.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Transactional
    public Order createOrder(String idempotencyKey, CreateOrderRequest request){
        try{
            Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString());
            order.setIdempotencyKey(idempotencyKey);
            order.setUserId(request.getUserId());
            order.setCreatedAt(LocalDateTime.now());
            order.setAmount(request.getAmount());
            order.setStatus(OrderStatus.CREATED);
            return orderRepository.save(order);
        } catch (DataIntegrityViolationException exception){
            return orderRepository.findByIdempotencyKey(idempotencyKey).orElseThrow(()-> exception);
        }
    }
}
