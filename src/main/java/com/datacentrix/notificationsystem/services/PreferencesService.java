package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.entity.*;
import com.datacentrix.notificationsystem.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.*;

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

    public void AddPreferences(Long userId, List<Preference> preferencesRequests) {

        try {
            List<Preference> preferences = preferencesRequests.stream()
                    .map(req -> Preference.builder()
                            .user(User.builder().id(userId).build())
                            .notification_type(req.getNotification_type())
                            .enabled(req.getEnabled())
                            .build())
                    .collect(Collectors.toList());

            preferenceRepository.saveAll(preferences);

        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error Saving: ", e);
        }

    }
}
