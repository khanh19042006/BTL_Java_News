package org.example.service;

import org.example.entity.News;

import java.util.List;

public interface HomeService {
    public List<News> getNewNews(); //Lấy 10 bài báo mới nhất
    public List<News> getHotNews(); //Lấy 10 bài báo có lượt view cao nhất
}
