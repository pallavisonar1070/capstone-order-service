package com.concepts.core.events;

import com.concepts.core.common.EventType;
import com.concepts.core.models.OrderStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
public class DomainEvent {
    private final String eventId;
    private final EventType eventType;
    private final String entityId;
    private final Instant occurredAt;
    private final Map<String, Object> payload;

    public DomainEvent(EventType eventType, String eventId, String orderId,Map<String, Object> payload) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.entityId = orderId;
        this.occurredAt = Instant.now();
        this.payload = payload;
    }
}
