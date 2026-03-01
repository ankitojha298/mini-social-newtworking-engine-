package com.connecthub.controllers;

import com.connecthub.models.User;
import com.connecthub.services.NavigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nav")
@RequiredArgsConstructor
public class NavController {
    private final NavigationService navigationService;

    @PostMapping("/visit/{id}")
    public ResponseEntity<?> visitProfile(@PathVariable Long id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        navigationService.visitProfile(currentUser.getId(), id);
        return ResponseEntity.ok("Profile pushed to Navigation Stack");
    }

    @PostMapping("/back")
    public ResponseEntity<?> goBack(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Long prevId = navigationService.goBack(currentUser.getId());
        if (prevId != null) {
            return ResponseEntity.ok(prevId);
        }
        return ResponseEntity.ok("Stack is empty");
    }
}
