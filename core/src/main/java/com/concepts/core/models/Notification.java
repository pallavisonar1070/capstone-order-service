package com.concepts.core.models;

import com.concepts.core.common.EventType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(
        name = "notifications",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"userId", "eventId"}
        )
)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String eventId;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    private Instant createdAt;
}
