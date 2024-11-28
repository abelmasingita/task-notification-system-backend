package com.datacentrix.notificationsystem.controllers;

import com.datacentrix.notificationsystem.entity.Preference;
import com.datacentrix.notificationsystem.services.PreferencesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preferences")
public class PreferencesController {

    @Autowired
    private final PreferencesService preferencesService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Preference>> getPreferences(@PathVariable Long userId) {
        return ResponseEntity.ok(preferencesService.getPreferencesForUser(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> AddPreferences(@PathVariable Long userId,@RequestBody List<Preference> request) {
        preferencesService.AddPreferences(userId, request);
        return ResponseEntity.ok().build();
    }
}
