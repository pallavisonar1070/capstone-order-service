package com.concepts.core.controller;

import com.concepts.core.models.Notification;
import com.concepts.core.models.NotificationStatus;
import com.concepts.core.repos.NotificationRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepo notificationRepo;

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam String userId) {
        return ResponseEntity.ok(
                notificationRepo.findByUserIdOrderByCreatedAtDesc(userId)
        );
    }

    @PostMapping("{id}/read")
    @Transactional
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        Notification notification = notificationRepo.findById(id).orElseThrow();
        notification.setStatus(NotificationStatus.READ);
        notificationRepo.save(notification);
        return ResponseEntity.ok().build();
    }

    @PostMapping("read-all")
    @Transactional
    public ResponseEntity<Void> markAllAsRead(@RequestParam String userId) {
        notificationRepo.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}
