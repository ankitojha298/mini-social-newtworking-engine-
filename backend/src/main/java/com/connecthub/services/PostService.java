package com.connecthub.services;

import com.connecthub.datastructures.NewsFeedList;
import com.connecthub.models.Post;
import com.connecthub.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    public Post createPost(Long authorId, String content) {
        Post post = new Post();
        post.setAuthorId(authorId);
        post.setContent(content);
        return postRepository.save(post);
    }

    public List<Post> generateFeed(Long userId) {
        // 1. Get friend IDs from Graph
        List<Long> friends = new ArrayList<>(userService.getDirectFriends(userId));
        friends.add(userId); // Include own posts

        // 2. Fetch posts from DB
        List<Post> recentPosts = postRepository.findByAuthorIdInOrderByCreatedAtDesc(friends);
        Map<Long, Post> postMap = recentPosts.stream().collect(Collectors.toMap(Post::getId, p -> p));

        // 3. Construct DLL for feed
        NewsFeedList feedList = new NewsFeedList();

        for (Post p : recentPosts) {
            feedList.addPostLast(p.getId());
        }

        return feedList.getFeed().stream().map(postMap::get).collect(Collectors.toList());
    }
}
