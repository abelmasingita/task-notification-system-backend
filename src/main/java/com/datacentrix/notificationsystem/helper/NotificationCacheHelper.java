package com.datacentrix.notificationsystem.helper;

import com.datacentrix.notificationsystem.entity.Notification;
import com.datacentrix.notificationsystem.enums.NotificationType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class NotificationCacheHelper {

    // Convert Notification to a Map with createdAt as String
    public static Map<String, Object> serializeNotification(Notification notification) {
        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("id", notification.getId());
        notificationMap.put("message", notification.getMessage());
        notificationMap.put("notificationType", notification.getNotificationType().name());
        notificationMap.put("createdAt", convertLocalDateTimeToString(notification.getCreatedAt()));
        return notificationMap;
    }

    // Convert LocalDateTime to String
    private static String convertLocalDateTimeToString(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return createdAt.format(formatter);
    }

    // Convert Map back to Notification
    public static Notification deserializeNotification(Map<String, Object> notificationMap) {
        LocalDateTime createdAt = convertStringToLocalDateTime((String) notificationMap.get("createdAt"));
        return Notification.builder()
                .id((Long) notificationMap.get("id"))
                .message((String) notificationMap.get("message"))
                .notificationType(NotificationType.valueOf((String) notificationMap.get("notificationType")))
                .createdAt(createdAt)
                .build();
    }

    // Convert String to LocalDateTime
    private static LocalDateTime convertStringToLocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}
