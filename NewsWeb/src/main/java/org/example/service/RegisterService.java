package org.example.service;

import org.example.dto.UserDTO;

public interface RegisterService {
    boolean register(UserDTO user);
    boolean checkEmail(String email);
}
