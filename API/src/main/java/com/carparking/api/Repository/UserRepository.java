package com.carparking.api.Repository;

import com.carparking.api.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserRepository extends Repository<User, Integer> {

    User findByPhoneNoAndPassword(String phoneNo, String password);
    List<User> findAllBy();
    User findByUserId(Integer userId);
    User findByPhoneNo(String phoneNo);
    User findByEmail(String email);
}
