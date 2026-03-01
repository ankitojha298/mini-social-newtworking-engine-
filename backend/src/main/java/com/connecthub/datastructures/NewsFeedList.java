package com.connecthub.datastructures;

import java.util.ArrayList;
import java.util.List;

class FeedNode {
    Long postId;
    FeedNode prev, next;
    public FeedNode(Long postId) { this.postId = postId; }
}

public class NewsFeedList {
    private FeedNode head, tail;

    // O(1) prepend operation for newest posts
    public void addPostFirst(Long postId) {
        FeedNode newNode = new FeedNode(postId);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
    }

    // O(1) appending for older posts during pagination
    public void addPostLast(Long postId) {
        FeedNode newNode = new FeedNode(postId);
        if (tail == null) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
    }

    // Traverse to render feed
    public List<Long> getFeed() {
        List<Long> feed = new ArrayList<>();
        FeedNode current = head;
        while (current != null) {
            feed.add(current.postId);
            current = current.next;
        }
        return feed;
    }
}
