package com.connecthub.controllers;

import com.connecthub.datastructures.SessionCache;
import com.connecthub.models.User;
import com.connecthub.repositories.UserRepository;
import com.connecthub.security.JwtUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    // In-memory advanced structure
    private final SessionCache sessionCache = new SessionCache();

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(request.getPassword()); // In real app, BCrypt hash this
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(u -> u.getPasswordHash().equals(request.getPassword()))
                .map(u -> {
                    String jwt = jwtUtils.generateJwtToken(u.getUsername());
                    sessionCache.login(jwt, u.getId());
                    return ResponseEntity.ok(new JwtResponse(jwt, u.getId(), u.getUsername()));
                })
                .orElse(ResponseEntity.status(401).body(null));
    }
}

@Data
class RegistrationRequest {
    private String username;
    private String email;
    private String password;
}

@Data
class LoginRequest {
    private String username;
    private String password;
}

@Data
class JwtResponse {
    private final String token;
    private final Long id;
    private final String username;
}
