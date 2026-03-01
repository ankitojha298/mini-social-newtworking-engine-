package com.connecthub.datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

public class SocialGraphTest {

    @Test
    public void testAddAndGetFriends() {
        SocialGraph graph = new SocialGraph();
        graph.addFriendship(1L, 2L);
        graph.addFriendship(1L, 3L);

        Set<Long> friendsOf1 = graph.getFriends(1L);
        assertEquals(2, friendsOf1.size());
        assertTrue(friendsOf1.contains(2L));
        assertTrue(friendsOf1.contains(3L));
        
        assertTrue(graph.getFriends(2L).contains(1L));
    }

    @Test
    public void testSuggestFriendsBFS() {
        SocialGraph graph = new SocialGraph();
        graph.addFriendship(1L, 2L);
        graph.addFriendship(2L, 3L);
        graph.addFriendship(3L, 4L);
        
        // 1 is friends with 2. 2 is friends with 3. 3 is friends with 4.
        // Friend of friend for 1 should be 3
        List<Long> suggestions = graph.suggestFriends(1L, 5);
        
        assertTrue(suggestions.contains(3L));
        assertFalse(suggestions.contains(2L)); // 2 is already a mutual friend
        assertEquals(2, suggestions.size()); // 3, and then 4 (via 3)
    }
}
