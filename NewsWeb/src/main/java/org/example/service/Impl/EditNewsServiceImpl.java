package org.example.service.Impl;

import org.example.dao.NewsDAO;
import org.example.dto.NewsDTO;
import org.example.service.EditNewsService;

import java.util.List;

public class EditNewsServiceImpl implements EditNewsService {
    private final NewsDAO newsDAO = new NewsDAO();

    @Override
    public NewsDTO getNewsById(String id){
        return newsDAO.getNewsById(id);
    }

    @Override
    public boolean updateNews(NewsDTO newsDTO){
        // Chỉ có thể chỉnh sửa
        // headline, category, short_description, content, thumbnail
        return newsDAO.updateNews(newsDTO);
    }
}
