package com.carparking.api.Service;

import com.carparking.api.Entity.History;
import com.carparking.api.Entity.Parking;
import com.carparking.api.Entity.User;
import com.carparking.api.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.json.simple.JSONObject;
import java.util.List;


@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    HistoryCrudRepository historyCrudRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    ParkingRepository parkingRepository;

    @Override
    public List<User> getAllUsers() {
        List<User> users =  userRepository.findAllBy();
        System.out.println("users : " + users.toString());
        return users;
    }

    public User userLogin(String phoneNo, String password) {
        User user= userRepository.findByPhoneNoAndPassword(phoneNo, password);
        return user;
    }

    public User getUser(Integer userId){
        User user = userRepository.findByUserId(userId);
        return user;
    }

    public User saveUser(User user){
        user.setBalance(0);
        userCrudRepository.save(user);
        return user;
    }

    @Override
    public List<History> getUserHistory(Integer userId) {
        List <History> userHistory = historyRepository.findHistoryByUserId(userId);
        for (int i = 0; i < userHistory.size(); i++) {
           History history = userHistory.get(i);
           Parking parking = parkingRepository.findByParkingId(history.getParkingId());
           String parkingName = parking.getParkingName();
           history.setParkingName(parkingName);

        }
        return userHistory;
    }

    @Override
    public User saveUserEmail(Integer userId, String email) {
        User user = userRepository.findByUserId(userId);
        user.setEmail(email);
        User savedUser = userCrudRepository.save(user);
        return savedUser;
    }
}
