package com.carparking.api.Service;

import com.carparking.api.Entity.History;
import com.carparking.api.Entity.User;

import java.util.List;

public interface IUserService {

    public Object getAllUsers();
    public Object userLogin(String phoneNo, String password);
    public Object getUser(Integer userId);
    public Object saveUser(User user);
    public Object getUserHistory(Integer userId);
    public Object saveUserEmail(Integer userId, String email);
}
