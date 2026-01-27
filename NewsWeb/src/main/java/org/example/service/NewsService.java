package org.example.service;

import org.example.dto.NewsDTO;

import java.util.List;

public interface NewsService {
    public List<NewsDTO> getNewsByAuthorId(String authorId);
    public NewsDTO getNewsById(String id);
}
