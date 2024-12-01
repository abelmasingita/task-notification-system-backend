package com.datacentrix.notificationsystem.repository;

import com.datacentrix.notificationsystem.entity.*;
import com.datacentrix.notificationsystem.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    List<Preference> findByUserId(Long userId);
    List<Preference> findByUser(User user);
    List<Preference> findByNotificationTypeAndIsEnabled(NotificationType notificationType, Boolean isEnabled);
    Optional<Preference> findByUserIdAndNotificationType(Long userId, NotificationType notificationType);
    boolean existsByUserIdAndNotificationType(Long userId, NotificationType notificationType);
}


