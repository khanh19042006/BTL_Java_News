package org.example.service;

import org.example.dto.NewsDTO;

import java.util.List;

public interface RecommendService {
    public List<NewsDTO> getRecommendNews(String userId);
}
