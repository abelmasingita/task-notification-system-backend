package com.datacentrix.notificationsystem.dto;

import lombok.Data;

@Data
public class UpdateNotificationPreferenceDto {
    private String notificationType; // The type of notification to update
    private Boolean isEnabled; // Whether the user wants to enable or disable the notification type
}
