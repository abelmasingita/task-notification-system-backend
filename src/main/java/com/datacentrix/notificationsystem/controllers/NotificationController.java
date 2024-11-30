package com.datacentrix.notificationsystem.controllers;

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
    @CrossOrigin(origins = "*")
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        notificationService.sendNotification(notification);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    @GetMapping()
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }
}
