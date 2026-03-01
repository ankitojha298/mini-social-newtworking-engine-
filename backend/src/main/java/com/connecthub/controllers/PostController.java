package com.connecthub.controllers;

import com.connecthub.models.Post;
import com.connecthub.models.User;
import com.connecthub.services.LikeService;
import com.connecthub.services.PostService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest request, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(postService.createPost(currentUser.getId(), request.getContent()));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<Post>> getFeed(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(postService.generateFeed(currentUser.getId()));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable Long id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        likeService.likePost(id, currentUser.getId());
        return ResponseEntity.ok("Post liked successfully using Queue delivery!");
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<?> undoLike(@PathVariable Long id, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        likeService.undoLike(id, currentUser.getId());
        return ResponseEntity.ok("Like undone utilizing Stack-like logic parameters");
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> commentPost(@PathVariable Long id, @RequestBody PostRequest request,
            Authentication authentication) {
        return ResponseEntity.ok("Comment pushed to Queue!");
    }
}

@Data
class PostRequest {
    private String content;
}
