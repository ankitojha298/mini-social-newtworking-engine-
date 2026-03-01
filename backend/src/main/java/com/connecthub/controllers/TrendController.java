package com.connecthub.controllers;

import com.connecthub.services.TrendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trends")
@RequiredArgsConstructor
public class TrendController {
    private final TrendingService trendingService;

    @GetMapping
    public ResponseEntity<String[]> getTopTrends() {
        return ResponseEntity.ok(trendingService.getTopTrends());
    }
}
