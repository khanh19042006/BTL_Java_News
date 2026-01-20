package org.example.service.Impl;

import javafx.fxml.FXML;
import org.example.dao.CategoryDAO;
import org.example.dao.NewsDAO;
import org.example.entity.Category;
import org.example.entity.News;
import org.example.service.HomeService;

import java.util.List;

public class HomeServiceimpl implements HomeService {

    private final int limit = 10;
    private final NewsDAO newsDAO = new NewsDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    public List<News> getNewNews(){
        return newsDAO.getNewNews(limit);
    }

    @Override
    public List<News> getHotNews(){
        return newsDAO.getHotNews(limit);
    }

    @Override
    public List<News> getNewsByFind(String headline){
return null;
    }

    @Override
    public List<News> getNewsByCategory(String category){
return null;
    }

    @Override
    public List<Category> getCategory(){
        return categoryDAO.getCategory();
    }
}
