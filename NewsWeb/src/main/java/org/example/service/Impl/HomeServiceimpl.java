package org.example.service.Impl;

import org.example.dao.CategoryDAO;
import org.example.dao.HistoryDAO;
import org.example.dao.NewsDAO;
import org.example.dto.CategoryDTO;
import org.example.dto.NewsDTO;
import org.example.service.HomeService;

import java.util.List;

public class HomeServiceimpl implements HomeService {

    private final int limit = 10;
    private final NewsDAO newsDAO = new NewsDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final HistoryDAO historyDAO = new HistoryDAO();

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
        List<NewsDTO> listHistoryNews = historyDAO.getHistoryNews(userId, limit);
        // Không đăng nhập thì đề cử mặc định
        if (userId == null)
            return this.getHotNews();
        // Lịch sử đọc ít quá thì bỏ qua, không phân tích
        if (listHistoryNews.size() < limit)
            return this.getHotNews();
        return newsDAO.recommendNews(userId, limit);
    }

    @Override
    public List<CategoryDTO> getCategory(){
        return categoryDAO.getCategory();
    }

    @Override
    public List<NewsDTO> getNewsNewByPage(int page){
        return newsDAO.getNewsNewByPage(page, limit);
    }

    @Override
    public int countTotalPageNews(){
        int totalNews = newsDAO.countTotalNews();
        int totalPage;
        // Tính số trang có thể có
        if (totalNews % limit == 0) totalPage = totalNews / limit;
        else totalPage = (totalNews / limit) + 1;

        return totalPage;
    }

    @Override
    public List<NewsDTO> getHotNewsByPage(int page){
        return newsDAO.getHotNewsByPage(page, limit);
    }

    @Override
    public boolean incrementViewCount(String newsId){
        return newsDAO.incrementViewCount(newsId);
    }
}
