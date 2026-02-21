package org.example.service.Impl;

import org.example.dao.NewsDAO;
import org.example.dao.UserDAO;
import org.example.dto.NewsDTO;
import org.example.dto.UserDTO;
import org.example.service.UpNewsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UpNewsServiceImpl implements UpNewsService {
    private final NewsDAO newsDAO = new NewsDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    public NewsDTO createNews(String headline, String category, String short_description,
                              String authorId, String content, String thumbnail){
        NewsDTO newsDTO = new NewsDTO();

        // Lấy user
        UserDTO userDTO = userDAO.getUserById(authorId);
        // Lấy thời gian
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentTime = now.format(formatter);

        newsDTO.setHeadline(headline);
        newsDTO.setCategory(category);
        newsDTO.setShort_description(short_description);
        newsDTO.setAuthors(userDTO.getUsername());
        newsDTO.setDate(currentTime);
        newsDTO.setViews(0);
        newsDTO.setContent(content);
        newsDTO.setThumbnail(thumbnail);
        newsDTO.setAuthorId(authorId);

        return newsDTO;
    }

    @Override
    public boolean upNews(NewsDTO newsDTO){
        return newsDAO.upNews(newsDTO);
    }
}
