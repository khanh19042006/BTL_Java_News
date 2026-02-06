package org.example.service.Impl;

import org.example.dao.CategoryDAO;
import org.example.dao.NewsDAO;
import org.example.dto.CategoryDTO;
import org.example.dto.NewsDTO;
import org.example.service.HomeService;

import java.util.List;

public class HomeServiceimpl implements HomeService {

    private final int limit = 10;
    private final NewsDAO newsDAO = new NewsDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    public List<NewsDTO> getNewNews(){
        return newsDAO.getNewNews(limit);
    }

    @Override
    public List<NewsDTO> getHotNews(){
        return newsDAO.getHotNews(limit);
    }

    @Override
    public List<NewsDTO> getNewsByFind(String keyword){
            return newsDAO.searchNews(keyword, limit);
    }

    @Override
    public List<NewsDTO> getNewsByCategory(String categoryName){

        return newsDAO.getNewsByCategory(categoryName, limit);
    }

    @Override
    public List<NewsDTO> getRecommendNews(String userId){
        return newsDAO.recommendNews(userId, limit);
    }

    @Override
    public List<CategoryDTO> getCategory(){
        return categoryDAO.getCategory();
    }
}
