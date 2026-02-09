package org.example.service.Impl;

import org.example.dao.HistoryDAO;
import org.example.dto.NewsDTO;
import org.example.service.HistoryService;

import java.util.List;

public class HistoryServiceImpl implements HistoryService {
    private final int limit = 10;
    private final HistoryDAO historyDAO = new HistoryDAO();

    @Override
    public boolean saveNews(String userId, String newsId){
        return historyDAO.saveNews(userId, newsId);
    }

    @Override
    public List<NewsDTO> getHistoryNews(String userId){
        return historyDAO.getHistoryNews(userId, this.limit);
    }
}
