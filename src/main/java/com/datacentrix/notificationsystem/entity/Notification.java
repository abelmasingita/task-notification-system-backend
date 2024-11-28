package com.datacentrix.notificationsystem.entity;


import com.datacentrix.notificationsystem.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifications" )
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type",nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

