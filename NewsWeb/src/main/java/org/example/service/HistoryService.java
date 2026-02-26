package org.example.service;

import org.example.dto.NewsDTO;

import java.util.List;

public interface HistoryService {
    public boolean saveNews(String userId, String newsId);
    public List<NewsDTO> getHistoryNews(String userId);
}
