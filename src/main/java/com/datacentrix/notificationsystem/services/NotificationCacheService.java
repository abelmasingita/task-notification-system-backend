package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.entity.Notification;
import com.datacentrix.notificationsystem.exception.RedisCacheException;
import com.datacentrix.notificationsystem.helper.NotificationCacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
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
        Logger logger = LoggerFactory.getLogger(this.getClass());

        try {
            Map<String, Object> notificationMap = NotificationCacheHelper.serializeNotification(notifications);
            redisTemplate.opsForList().rightPushAll(NOTIFICATIONS_CACHE_KEY, notificationMap);
            redisTemplate.expire(NOTIFICATIONS_CACHE_KEY, 3, TimeUnit.MINUTES);
        } catch (RedisConnectionFailureException e) {

            logger.error("Failed to cache notification. Redis is down: {}", e.getMessage(), e);
            throw new RedisCacheException("Redis connection failed while caching notifications", e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while caching notification: {}", e.getMessage(), e);
            throw new RedisCacheException("Unexpected error while caching notifications", e);
        }

    }

    public List<Notification> getNotificationsFromCache() {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        try {
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
        } catch (RedisConnectionFailureException e) {
            logger.error("Failed to retrieve notifications from cache. Redis is down: {}", e.getMessage(), e);
            throw new RedisCacheException("Redis connection failed while retrieving notifications", e);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while retrieving notifications from cache: {}", e.getMessage(), e);
            throw new RedisCacheException("Unexpected error while retrieving notifications", e);
        }
    }

}
