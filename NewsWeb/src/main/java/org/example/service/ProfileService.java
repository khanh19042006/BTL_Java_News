package org.example.service;

import org.example.dto.NewsDTO;
import org.example.dto.UserDTO;

import java.util.List;

public interface ProfileService {
    public List<NewsDTO> getNewsByUserId(String userId);
    public UserDTO getUserById(String id);
}
