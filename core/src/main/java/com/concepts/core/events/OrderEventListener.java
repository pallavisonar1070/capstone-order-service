package com.concepts.core.events;

import com.concepts.core.models.Order;
import com.concepts.core.models.OrderStatus;
import com.concepts.core.repos.OrderRepository;
import com.concepts.core.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventListener {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event){

        Order order = orderRepository.findByOrderId(event.getOrderId()).orElseThrow();
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        try {
            paymentService.processPayment(order);
            order.setStatus(OrderStatus.CONFIRMED);
        }catch (Exception ex){
            order.setStatus(OrderStatus.FAILED);
        }
        orderRepository.save(order);
    }
}
