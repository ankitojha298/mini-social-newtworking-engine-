package com.connecthub.datastructures;

import java.util.*;

public class SocialGraph {
    // Adjacency List: UserID -> Set of Friend UserIDs
    private final Map<Long, Set<Long>> adjList = new HashMap<>();

    public void addFriendship(Long u1, Long u2) {
        adjList.computeIfAbsent(u1, k -> new HashSet<>()).add(u2);
        adjList.computeIfAbsent(u2, k -> new HashSet<>()).add(u1);
    }

    public void removeFriendship(Long u1, Long u2) {
        Set<Long> list1 = adjList.get(u1);
        if (list1 != null) list1.remove(u2);
        Set<Long> list2 = adjList.get(u2);
        if (list2 != null) list2.remove(u1);
    }

    public Set<Long> getFriends(Long userId) {
        return adjList.getOrDefault(userId, Collections.emptySet());
    }

    // BFS to suggest friends (Friends of Friends)
    public List<Long> suggestFriends(Long userId, int maxSuggestions) {
        Set<Long> directFriends = adjList.getOrDefault(userId, Collections.emptySet());
        Set<Long> visited = new HashSet<>(directFriends);
        visited.add(userId);
        
        Queue<Long> queue = new LinkedList<>(directFriends);
        List<Long> suggestions = new ArrayList<>();

        while (!queue.isEmpty() && suggestions.size() < maxSuggestions) {
            Long current = queue.poll();
            for (Long friendOfFriend : adjList.getOrDefault(current, Collections.emptySet())) {
                if (!visited.contains(friendOfFriend)) {
                    visited.add(friendOfFriend);
                    suggestions.add(friendOfFriend);
                    queue.add(friendOfFriend);
                    if (suggestions.size() == maxSuggestions) break;
                }
            }
        }
        return suggestions;
    }
}
