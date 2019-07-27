package com.carparking.api.Service;

import com.carparking.api.Entity.User;
import com.carparking.api.Repository.UserCrudRepository;
import com.carparking.api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.json.simple.JSONObject;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserCrudRepository userCrudRepository;

    @Override
    public List<User> getAllUsers() {
        List<User> users =  userRepository.findAllBy();
        System.out.println("users : " + users.toString());
        return users;
    }

    public User userLogin(String phone_no, String password) {
        User user= userRepository.findByPhoneNoAndPassword(phone_no, password);
        return user;
    }

    public User getUser(Integer userId){
        User user = userRepository.findByUserId(userId);
        return user;
    }

    public User SaveUser(User user){
        System.out.println("------------------------------------------------------------------------" + user.toString());
        userCrudRepository.save(user);
        return user;
    }
}
