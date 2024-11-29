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

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private final NotificationCacheService notificationCacheService;

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

            // Cache the updated list of notifications
            notificationCacheService.cacheNotifications(notification);

        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error saving notification: ", e);
        }
    }

    // Method to get notifications for a specific user based on their preferences
    public List<Notification> getNotificationsForUser(Long userId) {

        try {
            // Find the user by ID
            Optional<User> user = userRepository.findById(userId);

            if (user.isPresent()) {
                // Fetch the preferences for the user
                List<Preference> preferences = notificationPreferenceRepository.findByUser(user.get());

                List<Notification> cachedNotifications = notificationCacheService.getNotificationsFromCache();
                if (!cachedNotifications.isEmpty()) {
                    //return notifications from cache
                    return cachedNotifications;
                }

                return preferences.stream()
                        .filter(Preference::getIsEnabled)
                        .map(pref -> {
                            NotificationType notificationType = NotificationType.valueOf(pref.getNotificationType().name());
                            return notificationRepository.findBynotificationType(notificationType);
                        })
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
            } else {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error fetching notifications: ", e);
            return Collections.emptyList();
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
