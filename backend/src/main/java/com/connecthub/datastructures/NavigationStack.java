package com.connecthub.datastructures;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class NavigationStack {
    // Thread-safe stacks mapped by User Session or ID
    private final ConcurrentHashMap<Long, Deque<Long>> userHistory = new ConcurrentHashMap<>();

    public void visitProfile(Long userId, Long profileId) {
        userHistory.computeIfAbsent(userId, k -> new ConcurrentLinkedDeque<>()).push(profileId);
    }

    public Long goBack(Long userId) {
        Deque<Long> history = userHistory.get(userId);
        if (history != null && !history.isEmpty()) {
            return history.pop(); // Returns previously visited profile
        }
        return null;
    }

    public boolean hasHistory(Long userId) {
        Deque<Long> history = userHistory.get(userId);
        return history != null && !history.isEmpty();
    }
}
