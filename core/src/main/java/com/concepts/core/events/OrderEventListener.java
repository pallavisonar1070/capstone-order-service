package com.concepts.core.events;

import com.concepts.core.common.EventType;
import com.concepts.core.models.Order;
import com.concepts.core.models.OrderStatus;
import com.concepts.core.models.ProcessEvent;
import com.concepts.core.repos.OrderRepository;
import com.concepts.core.repos.ProcessedEventRepo;
import com.concepts.core.services.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.DomainEvents;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final ProcessedEventRepo processedEventRepo;
    private final ApplicationEventPublisher publisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void handleOrderCreated(OrderCreatedEvent event) {


        Order order = orderRepository.findByOrderId(event.getOrderId()).orElseThrow();
        order.setStatus(OrderStatus.PAYMENT_PENDING);
        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            log.info("Order {} already processed with status {}",
                    order.getOrderId(), order.getStatus());
            return;
        }
        //Duplicate check
        if (processedEventRepo.existsById(event.getEventId())) {
            log.warn("Duplicate event detected : {} ", event.getEventId());
            return;
        }

        try {
            paymentService.processPayment(order);
            order.setStatus(OrderStatus.CONFIRMED);
        } catch (Exception ex) {
            order.setStatus(OrderStatus.FAILED);
        }
        orderRepository.save(order);
        ProcessEvent processed = new ProcessEvent();
        processed.setEventId(event.getEventId());
        processed.setProcessAt(LocalDateTime.now());
        processedEventRepo.save(processed);
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            publisher.publishEvent(new DomainEvent(
                    EventType.ORDER_CONFIRMED,
                    event.getEventId(),
                    order.getOrderId(),
                    Map.of(
                            "userId", order.getUserId(),
                            "amount", order.getAmount()
                    )
            ));
        }
    }
}
