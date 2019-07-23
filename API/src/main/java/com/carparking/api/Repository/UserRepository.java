package com.carparking.api.Repository;

import com.carparking.api.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface UserRepository extends Repository<User, Integer> {

    User findByPhoneAndAndPassword(String phone, String password);
    List<User> findAllBy();
    User findByUid(Integer uid);
}
