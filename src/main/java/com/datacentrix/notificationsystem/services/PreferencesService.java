package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.entity.*;
import com.datacentrix.notificationsystem.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferencesService {

    private final PreferenceRepository preferenceRepository;

    public List<Preference> getPreferencesForUser(Long userId) {

        try {
            return preferenceRepository.findByUserId(userId);
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error fetching preferences: ", e);
            return Collections.emptyList();
        }
    }

    public List<Preference> getAll() {

        try {
            return preferenceRepository.findAll();
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error fetching preferences: ", e);
            return Collections.emptyList();
        }
    }

    public void AddPreferences(List<Preference> preferencesRequests) {

        try {
            // Build preferences from the incoming request
            List<Preference> preferences = preferencesRequests.stream()
                    .map(req -> Preference.builder()
                            .user(User.builder().Id(req.getUser().getId()).build())
                            .notificationType(req.getNotificationType())
                            .isEnabled(req.getIsEnabled())
                            .createdAt(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            // Save preferences to the repository
            preferenceRepository.saveAll(preferences);

            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.info("Preferences saved successfully for {} users", preferences.size());

        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error Saving: ", e);
        }

    }
}
