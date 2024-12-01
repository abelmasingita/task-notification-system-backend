package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.dto.PrefernceDTO;
import com.datacentrix.notificationsystem.dto.UpdateNotificationPreferenceDto;
import com.datacentrix.notificationsystem.entity.Preference;
import com.datacentrix.notificationsystem.enums.NotificationType;
import com.datacentrix.notificationsystem.helper.PreferenceMapper;
import com.datacentrix.notificationsystem.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferencesService {

    private final PreferenceRepository preferenceRepository;

    @Autowired
    PreferenceMapper preferenceMapper;

    public List<PrefernceDTO> getPreferencesForUser(Long userId) {

        try {
            List<Preference> preferences = preferenceRepository.findByUserId(userId);
            return preferenceMapper.toDtoList(preferences);

        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error fetching preferences: ", e);
            return Collections.emptyList();
        }
    }

    public Optional<PrefernceDTO> getById(Long id) {
        try {
            return preferenceRepository.findById(id)
                    .map(preferenceMapper::toDTO);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error fetching preferences: ", e);
            return Optional.empty();
        }
    }


    public List<PrefernceDTO> getAll() {

        try {
            return  preferenceMapper.toDtoList(preferenceRepository.findAll());
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error fetching preferences: ", e);
            return Collections.emptyList();
        }
    }

    public String addPreferences(List<PrefernceDTO> preferencesRequests) {
        try {
            List<Preference> preferences = preferencesRequests.stream()
                    .map(preferenceMapper::fromDTO) // Map DTOs to entities
                    .filter(pref -> {
                        boolean exists = preferenceRepository.existsByUserIdAndNotificationType(
                                pref.getUser().getId(), pref.getNotificationType());
                        if (exists) {
                            Logger logger = LoggerFactory.getLogger(this.getClass());
                            logger.warn("User {} already subscribed to notification type {}",
                                    pref.getUser().getId(), pref.getNotificationType());
                        }
                        return !exists; // Add only if it doesn't exist
                    })
                    .collect(Collectors.toList());

            preferenceRepository.saveAll(preferences);
            return "Preferences saved successfully for " + preferences.size() + " users.";
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error saving preferences: ", e);
            throw new RuntimeException("Error saving preferences: " + e.getMessage(), e);
        }
    }

    public void updateNotificationPreference(Long userId, UpdateNotificationPreferenceDto updateDto) {
        // Validate if the notification type is valid
        NotificationType notificationType;
        try {
            notificationType = NotificationType.valueOf(updateDto.getNotificationType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid notification type: " + updateDto.getNotificationType());
        }

        // Find the user's preference record
        Preference preference = preferenceRepository
                .findByUserIdAndNotificationType(userId, notificationType)
                .orElseThrow(() -> new RuntimeException("Preference not found for this user and notification type"));

        // Update the isEnabled field
        preference.setIsEnabled(updateDto.getIsEnabled());
        preference.setUpdatedAt(LocalDateTime.now());

        // Save the updated preference
        preferenceRepository.save(preference);
    }

}
