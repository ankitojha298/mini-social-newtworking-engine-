package com.connecthub.controllers;

import com.connecthub.models.User;
import com.connecthub.services.UserService;
import com.connecthub.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchUsers(@RequestParam("q") String prefix) {
        return ResponseEntity.ok(userService.searchUsers(prefix));
    }

    @PostMapping("/{id}/friend")
    public ResponseEntity<?> addFriend(@PathVariable Long id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        userService.addFriend(currentUser.getId(), id);
        return ResponseEntity.ok("Friend added!");
    }

    @GetMapping("/{id}/suggestions")
    public ResponseEntity<List<User>> getSuggestions(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getSuggestedFriends(id));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long id) {
        List<Long> fIds = userService.getDirectFriends(id);
        return ResponseEntity.ok(userRepository.findAllById(fIds));
    }
}
