package org.example.service;

import org.example.entity.User;

public interface RegisterService {
    boolean register(User user);
    boolean checkEmail(String email);
}
