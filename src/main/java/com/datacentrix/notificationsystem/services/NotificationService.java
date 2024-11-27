package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.entity.*;
import com.datacentrix.notificationsystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(Notification request) {

        Notification notification = Notification.builder()
                .user(User.builder().id(request.getUser().getId()).build())
                .message(request.getMessage())
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications/" + request.getUser().getId(), notification);
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserId(userId);
    }
}
