package org.example.service;

import org.example.dto.NewsDTO;

import java.util.List;

public interface EditNewsService {
    public NewsDTO getNewsById(String id);
    public boolean updateNews(NewsDTO newsDTO);
}
