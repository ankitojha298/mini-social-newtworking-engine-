package com.connecthub.services;

import com.connecthub.datastructures.NavigationStack;
import org.springframework.stereotype.Service;

@Service
public class NavigationService {
    // In-memory stack for user navigation history
    private final NavigationStack navStack = new NavigationStack();

    public void visitProfile(Long userId, Long profileId) {
        navStack.visitProfile(userId, profileId);
    }

    public Long goBack(Long userId) {
        return navStack.goBack(userId);
    }
}
