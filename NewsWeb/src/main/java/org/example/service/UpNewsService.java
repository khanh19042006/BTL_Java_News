package org.example.service;

import org.example.dto.NewsDTO;

public interface UpNewsService {
    public NewsDTO createNews(String headline, String category, String short_description,
                              String authorId, String content, String thumbnail);
    public boolean upNews(NewsDTO newsDTO);
}
