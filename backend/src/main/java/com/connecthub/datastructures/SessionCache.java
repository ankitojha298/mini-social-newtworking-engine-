package com.connecthub.datastructures;

import java.util.concurrent.ConcurrentHashMap;

public class SessionCache {
    // Custom Hash Map mapping user JWT/Session Token to UserID
    private final ConcurrentHashMap<String, Long> activeSessions = new ConcurrentHashMap<>();

    public void login(String token, Long userId) {
        activeSessions.put(token, userId);
    }

    public Long validateSession(String token) {
        return activeSessions.get(token);
    }
    
    public void logout(String token) {
        activeSessions.remove(token);
    }
    
    public boolean isSessionActive(String token) {
        return activeSessions.containsKey(token);
    }
}
