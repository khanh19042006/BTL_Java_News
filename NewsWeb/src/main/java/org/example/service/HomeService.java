package org.example.service;

import org.example.dto.CategoryDTO;
import org.example.dto.NewsDTO;

import java.util.List;

public interface HomeService {
    public List<NewsDTO> getNewNews();                           // Lấy 10 bài báo mới nhất
    public List<NewsDTO> getHotNews();                           // Lấy 10 bài báo có lượt view cao nhất
    public List<NewsDTO> getNewsByFind(String headline);         // Lấy 10 bài viết theo thanh tìm kiếm
    public List<NewsDTO> getNewsByCategory(String category);     // Lấy 10 bài viết theo chủ đề
    public List<NewsDTO> getRecommendNews(String userId);        // Lấy 10 bài viết đề cử
    public List<CategoryDTO> getCategory();                      // Lấy toàn bộ danh sách Category
    public List<NewsDTO> getNewsNewByPage(int page);             // Lấy số
    public int countTotalPageNews();                             // Xem có tổng cộng bao nhiêu trang
}
