package com.connecthub.services;

import com.connecthub.datastructures.SocialGraph;
import com.connecthub.datastructures.UserTrie;
import com.connecthub.models.Friendship;
import com.connecthub.models.User;
import com.connecthub.repositories.FriendshipRepository;
import com.connecthub.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    // In-memory advanced structures
    private final SocialGraph socialGraph = new SocialGraph();
    private final UserTrie userTrie = new UserTrie();

    @PostConstruct
    public void hydrateStructures() {
        // Hydrate User Trie
        userRepository.findAll().forEach(u -> userTrie.insert(u.getUsername()));

        // Hydrate Social Graph
        friendshipRepository.findAll().forEach(f -> {
            if ("ACCEPTED".equals(f.getStatus())) {
                socialGraph.addFriendship(f.getUserId1(), f.getUserId2());
            }
        });
    }

    public List<String> searchUsers(String prefix) {
        return userTrie.autocomplete(prefix);
    }

    public void addFriend(Long u1, Long u2) {
        Friendship f = new Friendship();
        f.setUserId1(u1);
        f.setUserId2(u2);
        f.setStatus("ACCEPTED");
        friendshipRepository.save(f);
        socialGraph.addFriendship(u1, u2);
    }

    public List<User> getSuggestedFriends(Long userId) {
        List<Long> suggestions = socialGraph.suggestFriends(userId, 5);
        return userRepository.findAllById(suggestions);
    }

    public List<Long> getDirectFriends(Long userId) {
        return socialGraph.getFriends(userId).stream().toList();
    }
}
