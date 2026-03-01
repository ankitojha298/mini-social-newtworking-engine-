package com.connecthub.datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NotificationQueueTest {

    @Test
    public void testPushAndPollNotification() {
        NotificationQueue queue = new NotificationQueue();
        
        queue.pushNotification(1L, "User 2 liked your post");
        queue.pushNotification(1L, "User 3 commented");
        
        assertEquals(2, queue.getPendingNotificationCount(1L));
        
        assertEquals("User 2 liked your post", queue.pollNotification(1L));
        assertEquals("User 3 commented", queue.pollNotification(1L));
        
        assertNull(queue.pollNotification(1L)); // Empty queue
        assertEquals(0, queue.getPendingNotificationCount(1L));
    }
}
