package com.concepts.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter

public class OrderCreatedEvent {
    private final String orderId;
    private final String eventId;

    public OrderCreatedEvent(String orderId) {
        this.orderId = orderId;
        this.eventId = UUID.randomUUID().toString();
    }
}
