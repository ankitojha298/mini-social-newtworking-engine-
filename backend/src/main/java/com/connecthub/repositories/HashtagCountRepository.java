package com.connecthub.repositories;

import com.connecthub.models.HashtagCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagCountRepository extends JpaRepository<HashtagCount, Long> {
    HashtagCount findByTagName(String tagName);
}
