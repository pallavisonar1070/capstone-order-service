package com.concepts.core.listeners;

import com.concepts.core.common.EventType;
import com.concepts.core.events.DomainEvent;
import com.concepts.core.models.Notification;
import com.concepts.core.models.NotificationStatus;
import com.concepts.core.repos.NotificationRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class BullhornNotificationListener {
    private final NotificationRepo notificationRepo;

    @Async
    @EventListener
    @Transactional
    public void handleDomainEvent(DomainEvent event) {
        if (event.getEventType() != EventType.ORDER_CONFIRMED) {
            return;
        }
        String userId = event.getPayload().get("userId").toString();
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setEventId(event.getEventId());
        notification.setEventType(event.getEventType());
        notification.setMessage("You order is confirmed");
        notification.setStatus(NotificationStatus.UNREAD);
        notification.setCreatedAt(Instant.now());
        try {
            notificationRepo.save(notification);
            log.info("Order is confirmed for user : {}", userId);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Duplicate notification ignored");
        }
    }
}
