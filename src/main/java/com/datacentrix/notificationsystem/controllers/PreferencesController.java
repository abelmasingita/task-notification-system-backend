package com.datacentrix.notificationsystem.controllers;

import com.datacentrix.notificationsystem.entity.Preference;
import com.datacentrix.notificationsystem.services.PreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preferences")
public class PreferencesController {

    private final PreferencesService preferencesService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Preference>> getPreferences(@PathVariable Long userId) {
        return ResponseEntity.ok(preferencesService.getPreferencesForUser(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> savePreferences(
            @PathVariable Long userId,
            @RequestBody List<Preference> preferencesRequests) {
        preferencesService.savePreferences(userId, preferencesRequests);
        return ResponseEntity.ok().build();
    }
}
