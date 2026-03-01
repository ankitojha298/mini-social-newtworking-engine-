package com.connecthub.repositories;

import com.connecthub.models.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUserId1OrUserId2(Long userId1, Long userId2);

    Optional<Friendship> findByUserId1AndUserId2(Long userId1, Long userId2);
}
