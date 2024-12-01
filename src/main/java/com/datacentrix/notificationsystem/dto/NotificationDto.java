package com.datacentrix.notificationsystem.dto;

import com.datacentrix.notificationsystem.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long id;
    private String message;
    private NotificationType notificationType;
    private LocalDateTime createdAt ;
}
