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
    public Object getAllUsers() {
        List<User> users =  userRepository.findAllBy();
        if (users.size() > 0) {
            return users;
        }
        else {
            String error = "No users are present in the Database";
            return error;
        }
    }

    @Override
    public Object userLogin(String phoneNo, String password) {
        User user= userRepository.findByPhoneNoAndPassword(phoneNo, password);
        if (user == null) {
            User newUser = userRepository.findByPhoneNo(phoneNo);
            if (newUser == null) {
                String error = "You are not registered.Please Sign Up to register yourself";
                return error;
            }
            else {
                String error = "Password is incorrect.Please enter your registered password.";
                return error;
            }
        }
        else {
            return user;
        }
    }

    @Override
    public Object getUser(Integer userId){
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            String error = "User with given user Id does not exist. Please send registered User Id.";
            return error;
        }
        else {
            return user;
        }
    }

    @Override
    public Object saveUser(User user){
        String error = "Something went wrong.Please try again.";
        if (user.getName() != null) {
            if (user.getPhoneNo() != null) {
                if (user.getPassword() != null) {
                    User existingUser = userRepository.findByPhoneNo(user.getPhoneNo());
                    if (existingUser == null) {
                        if (user.getEmail()!= null) {
                            existingUser = userRepository.findByEmail(user.getEmail());
                            if (existingUser != null) {
                                error = "Entered email is already registered. " + "\nPlease Login to your existing account or create a new account with a new email.";
                                return error;
                            }
                        }
                        user.setBalance(0);
                        User savedUser = userCrudRepository.save(user);
                        if (savedUser != null) {
                            return savedUser;
                        } else {
                            return error;
                        }
                    }
                    else {
                        error = "Entered phone number is already registered. " + "\nPlease Login to your existing account or create a new account with a new phone number.";
                        return error;
                    }
                }
                else {
                    error = "Please enter password to complete Sign Up";
                    return error;
                }
            }
            else {
                error = "Please enter phone number to complete Sign Up";
                return error;
            }
        }
        else {
            error = "Please enter name to complete Sign Up";
            return error;
        }

    }

    @Override
    public Object getUserHistory(Integer userId) {
        List <History> userHistory = historyRepository.findHistoryByUserId(userId);
        if (userHistory.size() > 0 ) {
            for (int i = 0; i < userHistory.size(); i++) {
                History history = userHistory.get(i);
                Parking parking = parkingRepository.findByParkingId(history.getParkingId());
                String parkingName = parking.getParkingName();
                history.setParkingName(parkingName);

            }
            return userHistory;
        }
        else {
            String error = "Sorry, You had no bookings till now.";
            return error;
        }
    }

    @Override
    public Object saveUserEmail(Integer userId, String email) {
        User user = userRepository.findByUserId(userId);
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            user.setEmail(email);
            User savedUser = userCrudRepository.save(user);
            if (savedUser != null) {
                return savedUser;
            }
            else {
                String error = "Something went wrong.Please try again.";
                return error;
            }
        }
        else {
            String error = "User with given email already exist. Please enter another email.";
            return error;
        }
    }

}
