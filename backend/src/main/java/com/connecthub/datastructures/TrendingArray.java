package com.connecthub.datastructures;

public class TrendingArray {
    // Fixed size array for top 10 trends
    private final String[] topTrends = new String[10];
    private final long[] mentionCounts = new long[10];
    
    public void updateTrends(String[] newTrends, long[] newCounts) {
        int limit = Math.min(10, Math.min(newTrends.length, newCounts.length));
        System.arraycopy(newTrends, 0, topTrends, 0, limit);
        System.arraycopy(newCounts, 0, mentionCounts, 0, limit);
        
        // Nullify any remaining old trends
        for (int i = limit; i < 10; i++) {
            topTrends[i] = null;
            mentionCounts[i] = 0;
        }
    }

    public String[] getTopTrends() {
        return topTrends;
    }
    
    public long[] getMentionCounts() {
        return mentionCounts;
    }
}
