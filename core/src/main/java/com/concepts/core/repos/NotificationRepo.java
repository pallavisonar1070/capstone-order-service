package com.concepts.core.repos;

import com.concepts.core.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    @Modifying
    @Query("update Notification n set n.status = 'READ' where n.userId = :userId")
    void markAllAsRead(String userId);
}
