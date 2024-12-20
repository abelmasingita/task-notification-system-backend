package com.datacentrix.notificationsystem.repository;

import com.datacentrix.notificationsystem.entity.Notification;
import com.datacentrix.notificationsystem.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository  extends JpaRepository<Notification, Long> {
    List<Notification> findBynotificationType(NotificationType notificationType);
}



