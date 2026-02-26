package org.example.service.Impl;

import org.example.dao.NewsDAO;
import org.example.dto.NewsDTO;
import org.example.service.NewsService;

import java.util.List;

public class NewsServiceImpl implements NewsService {
    private final NewsDAO newsDAO = new NewsDAO();

    @Override
    public List<NewsDTO> getNewsByAuthorId(String authorId){
        return newsDAO.getNewsByAuthorId(authorId);
    }

    @Override
    public NewsDTO getNewsById(String id){
        return newsDAO.getNewsById(id);
    }
}
