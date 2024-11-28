package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.entity.*;
import com.datacentrix.notificationsystem.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        try {
            Notification notification = Notification.builder()
                    .user(User.builder().id(request.getUser().getId()).build())
                    .message(request.getMessage())
                    .read(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/notifications/" + request.getUser().getId(), notification);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error saving notification: ", e);
        }
    }

    public List<Notification> getNotificationsForUser(Long userId) {

        try {
            return notificationRepository.findByUserId(userId);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error fetching notifications: ", e);
            return Collections.emptyList();
        }
    }

//    public List<Notification> GetAll() {
//        try {
//            return notificationRepository.findAll();
//        } catch (Exception e) {
//            Logger logger = LoggerFactory.getLogger(this.getClass());
//            logger.error("Error fetching notifications: ", e);
//            return Collections.emptyList();
//        }
//    }
}
