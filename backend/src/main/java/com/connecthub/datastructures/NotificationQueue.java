package com.connecthub.datastructures;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NotificationQueue {
    // Thread-safe map of Queues per UserID
    private final ConcurrentHashMap<Long, Queue<String>> userQueues = new ConcurrentHashMap<>();

    public void pushNotification(Long userId, String message) {
        userQueues.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>()).offer(message);
    }

    public String pollNotification(Long userId) {
        Queue<String> queue = userQueues.get(userId);
        return (queue != null) ? queue.poll() : null;
    }

    public int getPendingNotificationCount(Long userId) {
        Queue<String> queue = userQueues.get(userId);
        return (queue != null) ? queue.size() : 0;
    }
}
