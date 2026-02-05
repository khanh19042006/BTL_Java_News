package org.example.service.Impl;

import org.example.dao.NewsDAO;
import org.example.dao.UserDAO;
import org.example.dto.NewsDTO;
import org.example.dto.UserDTO;
import org.example.service.ProfileService;
import org.example.utils.CheckNullUtils;

import java.util.List;

public class ProfileServiceImpl implements ProfileService {

    private final UserDAO userDao = new UserDAO();
    private final NewsDAO newsDAO = new NewsDAO();
    private final int limit = 10;

    @Override
    public List<NewsDTO> getNewsByUserId(String userId) {
        UserDTO user = this.getUserById(userId);
        // Kiểm tra xem có thuộc tính nào bị null hay không
        if (CheckNullUtils.checkNullUser(user)) throw new RuntimeException("User not found");
        // Nếu là admin thì trả toàn bộ bài báo
        if (user.getRole().equalsIgnoreCase("ADMIN")){
            return newsDAO.getNewNews(limit);
        }
        // Không phải admin thì trả bài báo theo id user
        return newsDAO.getNewsByAuthorId(userId);
    }

    @Override
    public UserDTO getUserById(String id){
        return userDao.getUserById(id);
    }
}
