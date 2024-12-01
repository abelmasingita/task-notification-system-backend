package com.datacentrix.notificationsystem.dto;

import com.datacentrix.notificationsystem.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrefernceDTO {

    private Long Id;
    private UserDTO user;
    private NotificationType notificationType;
    private Boolean isEnabled = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt  = LocalDateTime.now();
}
