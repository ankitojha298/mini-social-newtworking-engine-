package com.connecthub.services;

import com.connecthub.models.Like;
import com.connecthub.models.Post;
import com.connecthub.repositories.LikeRepository;
import com.connecthub.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    @Transactional
    public void likePost(Long postId, Long userId) {
        likeRepository.findByPostIdAndUserId(postId, userId).ifPresentOrElse(
                like -> {
                    /* Already liked */ },
                () -> {
                    Like newLike = new Like();
                    newLike.setPostId(postId);
                    newLike.setUserId(userId);
                    likeRepository.save(newLike);

                    // Trigger queue notification
                    Optional<Post> optPost = postRepository.findById(postId);
                    optPost.ifPresent(post -> {
                        if (!post.getAuthorId().equals(userId)) {
                            notificationService.pushNotification(
                                    post.getAuthorId(),
                                    "LIKE",
                                    postId,
                                    "User ID " + userId + " liked your post");
                        }
                    });
                });
    }

    @Transactional
    public void undoLike(Long postId, Long userId) {
        likeRepository.deleteByPostIdAndUserId(postId, userId);
    }
}
