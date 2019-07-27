package com.carparking.api.Service;

import com.carparking.api.Entity.User;
import com.carparking.api.Repository.UserCrudRepository;
import com.carparking.api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        User user= userRepository.findByPhoneAndAndPassword(phone_no, password);
        return user;
    }

    public User getUser(Integer uid){
        User user = userRepository.findByUid(uid);
        return user;
    }

    public String userSignUp(String name, String password, Integer balance, String phone_no, String email){
        Integer id = null;
        User newUser = new User(name, id, password, balance, phone_no, email);
        User savedUser = userCrudRepository.save(newUser);
        if(savedUser != null) {
            return "SignUp successful";
        }
        else {
            return "SignUp unsuccessful";
        }
    }
}
