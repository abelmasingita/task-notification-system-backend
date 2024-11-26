package com.datacentrix.notificationsystem.services;

import lombok.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String userId, String message) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", message);
    }
}
