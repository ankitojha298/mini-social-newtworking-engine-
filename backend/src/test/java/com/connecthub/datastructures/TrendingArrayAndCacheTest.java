package com.connecthub.datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrendingArrayAndCacheTest {

    @Test
    public void testTrendingArray() {
        TrendingArray trending = new TrendingArray();
        String[] trends = {"#java", "#spring", "#react", "#docker"};
        long[] counts = {100, 90, 80, 70};
        
        trending.updateTrends(trends, counts);
        
        String[] topTrends = trending.getTopTrends();
        assertEquals("#java", topTrends[0]);
        assertEquals("#docker", topTrends[3]);
        assertNull(topTrends[4]); // Unfilled remains null
    }

    @Test
    public void testSessionCache() {
        SessionCache cache = new SessionCache();
        String token = "jwt-token-123";
        
        cache.login(token, 101L);
        assertTrue(cache.isSessionActive(token));
        assertEquals(101L, cache.validateSession(token));
        
        cache.logout(token);
        assertFalse(cache.isSessionActive(token));
        assertNull(cache.validateSession(token));
    }
}
