package org.example.service;

import org.example.dto.UserDTO;

public interface RegisterService {
    public boolean register(UserDTO user);
    public boolean checkEmail(String email);
    public boolean checkUsername(String username);
    public boolean checkPassword (String password);
}
