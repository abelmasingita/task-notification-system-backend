package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.entity.*;
import com.datacentrix.notificationsystem.enums.NotificationType;
import com.datacentrix.notificationsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PreferenceRepository notificationPreferenceRepository;

    @Autowired
    private UserRepository userRepository;


    public void sendNotification(Notification request) {

        try {
            Notification notification = new Notification();
            notification.setMessage(request.getMessage());
            notification.setCreatedAt(request.getCreatedAt());
            notification.setNotificationType(request.getNotificationType());

            notificationRepository.save(notification);


            // Identify relevant users based on their preferences for this notification type
            List<User> relevantUsers = getRelevantUsersForNotification(String.valueOf(request.getNotificationType()));

            // Send the notification to the relevant users using WebSocket
           for (User user : relevantUsers) {
                String destination = "/topic/notifications/" + user.getId();
                messagingTemplate.convertAndSend(destination, notification);
           }

        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error saving notification: ", e);
        }
    }

    // Method to get notifications for a specific user based on their preferences
    public List<Notification> getNotificationsForUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Preference> preferences = notificationPreferenceRepository.findByUser(user.get());
            List<Notification> notifications = preferences.stream()
                    //.filter(pref -> pref.isEnabled())
                    .map(pref -> notificationRepository.findBynotificationType(String.valueOf(pref.getNotificationType())))
                    .flatMap(List::stream)
                    .toList();
            return notifications;
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public List<Notification> getAll() {

        try {
            return notificationRepository.findAll();
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error fetching notifications: ", e);
            return Collections.emptyList();
        }
    }

    // Get relevant users based on their notification preferences
    public List<User> getRelevantUsersForNotification(String notificationType) {

        if (notificationType == null || notificationType.isEmpty()) {
            throw new IllegalArgumentException("Notification type must not be null or empty.");
        }

        NotificationType notificationEnum;
        try {
            notificationEnum = NotificationType.valueOf(notificationType);
        } catch (IllegalArgumentException e) {
            // Handle case when the provided String does not match any enum value
            throw new IllegalArgumentException("Invalid notification type: " + notificationType, e);
        }

        List<Preference> preferences = notificationPreferenceRepository.findByNotificationType(notificationEnum);

        return preferences.stream()
                .map(Preference::getUser)  // Get the associated user
                .collect(Collectors.toList());
    }
}
