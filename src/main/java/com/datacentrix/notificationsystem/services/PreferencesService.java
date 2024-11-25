package com.datacentrix.notificationsystem.services;

import com.datacentrix.notificationsystem.entity.*;
import com.datacentrix.notificationsystem.repository.PreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreferencesService {

    private final PreferenceRepository preferenceRepository;

    public List<Preference> getPreferencesForUser(Long userId) {
        return preferenceRepository.findByUserId(userId);
    }

    public void savePreferences(Long userId, List<Preference> preferencesRequests) {
        List<Preference> preferences = preferencesRequests.stream()
                .map(req -> Preference.builder()
                        .user(User.builder().id(userId).build())
                        .notificationType(req.getNotificationType())
                        .enabled(req.getEnabled())
                        .build())
                .collect(Collectors.toList());

        preferenceRepository.saveAll(preferences);
    }
}
