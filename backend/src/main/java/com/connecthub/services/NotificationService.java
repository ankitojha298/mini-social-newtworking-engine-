package com.connecthub.services;

import com.connecthub.datastructures.NotificationQueue;
import com.connecthub.models.Notification;
import com.connecthub.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // In-memory advanced structure
    private final NotificationQueue notificationQueue = new NotificationQueue();

    public void pushNotification(Long recipientId, String type, Long referenceId, String message) {
        // 1. Save to DB
        Notification n = new Notification();
        n.setRecipientId(recipientId);
        n.setType(type);
        n.setReferenceId(referenceId);
        notificationRepository.save(n);

        // 2. Push to Queue (Memory)
        notificationQueue.pushNotification(recipientId, message);

        // 3. Dispatch real-time WebSocket event
        messagingTemplate.convertAndSend("/topic/notifications/" + recipientId, message);
    }

    public String pollNextNotification(Long userId) {
        return notificationQueue.pollNotification(userId);
    }

    public List<Notification> getPersistedUnread(Long userId) {
        return notificationRepository.findByRecipientIdAndIsReadFalseOrderByCreatedAtAsc(userId);
    }
}
