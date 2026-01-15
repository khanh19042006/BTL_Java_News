package org.example.service.Impl;

import org.example.dao.UserDAO;
import org.example.entity.User;
import org.example.service.RegisterService;

public class RegisterServiceImpl implements RegisterService {

    private final UserDAO userDAO;

    public RegisterServiceImpl(){
        this.userDAO = new UserDAO();
    }

    @Override
    public boolean register(User user){
        boolean isExist = userDAO.checkUsername(user.getUsername());
        if (isExist) return false;
        //Thiếu check mail có định dạng phù hợp
        else{
            userDAO.createUser(user);
            return true;
        }
    }
}
