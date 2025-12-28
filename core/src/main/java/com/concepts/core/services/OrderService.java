package com.concepts.core.services;

import com.concepts.core.dtos.CreateOrderRequest;
import com.concepts.core.events.OrderCreatedEvent;
import com.concepts.core.exception.FeatureDisabledException;
import com.concepts.core.models.Order;
import com.concepts.core.models.OrderStatus;
import com.concepts.core.repos.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final FeatureFlagService featureFlagService;

    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher applicationEventPublisher, FeatureFlagService featureFlagService) {
        this.orderRepository = orderRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.featureFlagService = featureFlagService;
    }

    @Transactional
    public Order createOrder(String idempotencyKey, CreateOrderRequest request) {
        if (!featureFlagService.isEnabled("ORDER_CREATION")) {
            log.warn("Order creation disabled by feature flag");
            throw new FeatureDisabledException("Order creation is temporarily disabled");
        }

        Optional<Order> existing =
                orderRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            log.warn("Duplicate idempotency key {}", idempotencyKey);
            return existing.get();
        }

        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setIdempotencyKey(idempotencyKey);
        order.setUserId(request.getUserId());
        order.setCreatedAt(LocalDateTime.now());
        order.setAmount(request.getAmount());
        order.setStatus(OrderStatus.CREATED);
        Order saved = orderRepository.save(order);
        OrderCreatedEvent event = new OrderCreatedEvent(saved.getOrderId());
        applicationEventPublisher.publishEvent(event);
        //applicationEventPublisher.publishEvent(event);
        return saved;

    }
}
