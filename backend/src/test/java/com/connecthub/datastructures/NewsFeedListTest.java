package com.connecthub.datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class NewsFeedListTest {

    @Test
    public void testAddAndGetFeed() {
        NewsFeedList feed = new NewsFeedList();
        
        // Add chronologically newest at the head (O(1))
        feed.addPostFirst(3L);
        feed.addPostFirst(2L);
        feed.addPostFirst(1L);
        
        List<Long> result = feed.getFeed();
        assertEquals(3, result.size());
        assertEquals(1L, result.get(0));
        assertEquals(2L, result.get(1));
        assertEquals(3L, result.get(2));
    }
}
