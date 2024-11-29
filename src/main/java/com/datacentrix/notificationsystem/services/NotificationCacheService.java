package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.entity.Notification;
import com.datacentrix.notificationsystem.helper.NotificationCacheHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String NOTIFICATIONS_CACHE_KEY = "recent_notifications";


    @Autowired
    public NotificationCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheNotifications(Notification notifications) {
        Map<String, Object> notificationMap = NotificationCacheHelper.serializeNotification(notifications);
        redisTemplate.opsForList().rightPushAll(NOTIFICATIONS_CACHE_KEY, notificationMap);
        redisTemplate.expire(NOTIFICATIONS_CACHE_KEY, 3, TimeUnit.MINUTES);
    }

    public List<Notification> getNotificationsFromCache() {
        // Fetch notifications from Redis (stored as Map)
        List<Object> notificationMaps = redisTemplate.opsForList().range(NOTIFICATIONS_CACHE_KEY, 0, -1);

        List<Notification> notifications = new ArrayList<>();
        if (notificationMaps != null) {
            // Convert each Map back to a Notification object
            for (Object notificationMap : notificationMaps) {
                notifications.add(NotificationCacheHelper.deserializeNotification((Map<String, Object>) notificationMap));
            }
        }
        return notifications;
    }


    public void clearCache() {
        // Clear the cached notifications
        redisTemplate.delete(NOTIFICATIONS_CACHE_KEY);
    }

}
