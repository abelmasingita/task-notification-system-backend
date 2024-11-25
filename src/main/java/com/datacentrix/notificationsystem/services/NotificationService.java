package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.entity.*;
import com.datacentrix.notificationsystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Long userId, String message) {
        Notification notification = Notification.builder()
                .user(User.builder().id(userId).build())
                .message(message)
                .build();

        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findAllById(Collections.singleton(userId));
    }
}
