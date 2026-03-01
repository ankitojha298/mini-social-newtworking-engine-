package com.connecthub.services;

import com.connecthub.datastructures.TrendingArray;
import com.connecthub.models.HashtagCount;
import com.connecthub.repositories.HashtagCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrendingService {
    private final HashtagCountRepository hashtagRepo;
    private final TrendingArray trendingArray = new TrendingArray();

    @PostConstruct
    public void init() {
        refreshTrends();
    }

    // Refresh every hour or so, but we'll do it on-demand for demo
    public void refreshTrends() {
        Page<HashtagCount> topTags = hashtagRepo.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mentionCount")));

        List<HashtagCount> list = topTags.getContent();
        String[] tags = new String[list.size()];
        long[] counts = new long[list.size()];

        for (int i = 0; i < list.size(); i++) {
            tags[i] = list.get(i).getTagName();
            counts[i] = list.get(i).getMentionCount();
        }

        trendingArray.updateTrends(tags, counts);
    }

    public String[] getTopTrends() {
        return trendingArray.getTopTrends();
    }
}
