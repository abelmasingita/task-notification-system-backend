package com.datacentrix.notificationsystem.controllers;

import com.datacentrix.notificationsystem.dto.NotificationDto;
import com.datacentrix.notificationsystem.entity.Notification;
import com.datacentrix.notificationsystem.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@RequestBody NotificationDto notification) {
        return ResponseEntity.ok(notificationService.sendNotification(notification));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    @GetMapping()
    public ResponseEntity<List<NotificationDto>> getAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }
}
