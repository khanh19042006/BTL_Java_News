package org.example.service;

import org.example.entity.Category;
import org.example.entity.News;

import java.util.List;

public interface HomeService {
    public List<News> getNewNews(); //Lấy 10 bài báo mới nhất
    public List<News> getHotNews(); //Lấy 10 bài báo có lượt view cao nhất
    public List<News> getNewsByFind(String headline); //Lấy 10 bài viết theo thanh tìm kiếm
    public List<News> getNewsByCategory(String category);   //Lấy 10 bài viết theo chủ đề
    public List<Category> getCategory();    //Lấy toàn bộ danh sách Category
}
