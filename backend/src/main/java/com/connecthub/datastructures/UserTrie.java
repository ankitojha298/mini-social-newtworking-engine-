package com.connecthub.datastructures;

import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEndOfWord;
    String fullUsername;
}

public class UserTrie {
    private final TrieNode root = new TrieNode();

    public void insert(String username) {
        if (username == null || username.trim().isEmpty()) return;
        TrieNode current = root;
        for (char ch : username.toLowerCase().toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
        current.fullUsername = username;
    }

    public List<String> autocomplete(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) return Collections.emptyList();
        
        TrieNode current = root;
        for (char ch : prefix.toLowerCase().toCharArray()) {
            current = current.children.get(ch);
            if (current == null) return Collections.emptyList();
        }
        return getWordsFromNode(current, new ArrayList<>());
    }

    private List<String> getWordsFromNode(TrieNode node, List<String> results) {
        if (node.isEndOfWord) results.add(node.fullUsername);
        for (TrieNode child : node.children.values()) {
            getWordsFromNode(child, results);
        }
        return results;
    }
}
