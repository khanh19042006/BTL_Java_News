package org.example.service.Impl;

import javafx.fxml.FXML;
import org.example.dao.NewsDAO;
import org.example.entity.News;
import org.example.service.HomeService;

import java.util.List;

public class HomeServiceimpl implements HomeService {

    private final int limit = 10;
    private final NewsDAO dao = new NewsDAO();

    @Override
    public List<News> getNewNews(){
        return dao.getNewNews(limit);
    }

    @Override
    public List<News> getHotNews(){
        return dao.getHotNews(limit);
    }

    @Override
    public List<News> getNewsByFind(String headline){

    }

    @Override
    public List<News> getNewsByCategory(String category){

    }
}
