package com.connecthub.controllers;

import com.connecthub.models.User;
import com.connecthub.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<String> pollNotification(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        String notif = notificationService.pollNextNotification(currentUser.getId());
        return ResponseEntity.ok(notif != null ? notif : "No new notifications");
    }
}
