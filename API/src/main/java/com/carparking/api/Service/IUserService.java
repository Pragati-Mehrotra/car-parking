package com.carparking.api.Service;

import com.carparking.api.Entity.User;

import java.util.List;

public interface IUserService {

    public List<User> getAllUsers();
    public User userLogin(String phoneNo, String password);
    public User getUser(Integer userId);
    public User saveUser(User user);
}
