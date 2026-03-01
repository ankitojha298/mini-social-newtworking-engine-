package com.connecthub.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "friendships")
@IdClass(FriendshipId.class)
public class Friendship {
    @Id
    @Column(name = "user_id_1")
    private Long userId1;

    @Id
    @Column(name = "user_id_2")
    private Long userId2;

    @Column(length = 20)
    private String status = "ACCEPTED";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

class FriendshipId implements java.io.Serializable {
    private Long userId1;
    private Long userId2;
}
