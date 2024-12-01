package com.datacentrix.notificationsystem.controllers;

import com.datacentrix.notificationsystem.dto.PrefernceDTO;
import com.datacentrix.notificationsystem.dto.UpdateNotificationPreferenceDto;
import com.datacentrix.notificationsystem.services.PreferencesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preferences")
public class PreferencesController {

    private final PreferencesService preferencesService;

    @GetMapping()
    public ResponseEntity<?> getAll() {
        try {
            List<PrefernceDTO> preferences = preferencesService.getAll();

            if (preferences.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "No preferences found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            return ResponseEntity.ok(preferences);

        } catch (Exception e) {
            // Return a 500 Internal Server Error with a detailed error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error retrieving preferences: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{Id}")
    public ResponseEntity<?> getById(@PathVariable Long Id) {
        try {
            Optional<PrefernceDTO> preference = preferencesService.getById(Id);

            return preference.map(ResponseEntity::ok)
                    // If preference is not found, return 404 Not Found with error message
                    .orElseGet(() -> {
                        Map<String, String> errorResponse = new HashMap<>();
                        errorResponse.put("error", "Preference with ID " + Id + " not found.");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body((PrefernceDTO) errorResponse);
                    });
        } catch (Exception e) {
            // Return a 500 Internal Server Error with a detailed error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error retrieving preference: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> GetPreferences(@PathVariable Long userId) {

        try {
            List<PrefernceDTO> preferences = preferencesService.getPreferencesForUser(userId);
            return ResponseEntity.ok(preferences);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error retrieving notification preferences: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<?> addPreferences(@RequestBody List<PrefernceDTO> request) {
        try {
            String message = preferencesService.addPreferences(request);
            return ResponseEntity.ok(Collections.singletonMap("message", message));
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error("Error adding preferences: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Error adding preferences: " + e.getMessage()));
        }
    }


    @PutMapping("/{userId}")
    public ResponseEntity<String> updateNotificationPreferences(
            @PathVariable Long userId,
            @RequestBody UpdateNotificationPreferenceDto updateDto) {
        try {
            preferencesService.updateNotificationPreference(userId, updateDto);
            return ResponseEntity.ok("Notification preference updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating notification preference: " + e.getMessage());
        }
    }

}
