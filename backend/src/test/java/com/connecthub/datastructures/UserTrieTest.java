package com.connecthub.datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class UserTrieTest {

    @Test
    public void testInsertAndAutocomplete() {
        UserTrie trie = new UserTrie();
        trie.insert("Alice");
        trie.insert("Alan");
        trie.insert("Alexander");
        trie.insert("Bob");

        List<String> results = trie.autocomplete("Al");
        assertEquals(3, results.size());
        assertTrue(results.contains("Alice"));
        assertTrue(results.contains("Alan"));
        assertTrue(results.contains("Alexander"));

        List<String> resultsBob = trie.autocomplete("B");
        assertEquals(1, resultsBob.size());
        assertEquals("Bob", resultsBob.get(0));

        List<String> resultsEmpty = trie.autocomplete("Z");
        assertTrue(resultsEmpty.isEmpty());
    }
}
