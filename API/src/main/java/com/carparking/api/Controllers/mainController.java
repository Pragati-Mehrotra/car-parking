package com.carparking.api.Controllers;

import com.carparking.api.Entity.User;
import com.carparking.api.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

// import com.carparking.api.Pojo.userLoginRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class mainController {

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    UserServiceImpl userService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(method = RequestMethod.GET ,value = "/user/login")
    public @ResponseBody User userLogin(@RequestBody String request) throws IOException{

        JsonNode reqNode = mapper.readTree(request);
        String phone_no = reqNode.get("phone_no").asText();
        String password = reqNode.get("password").asText();
        return userService.userLogin(phone_no, password);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/signUp")
    public String userSignUp(@RequestBody String request) throws IOException{

        JsonNode reqNode = mapper.readTree(request);
        String name = reqNode.get("name").asText();
        String password = reqNode.get("password").asText();
        Integer balance = reqNode.get("balance").asInt(0);
        String phone_no = reqNode.get("phone_no").asText();
        String email = reqNode.get("email").asText();
        return userService.userSignUp(name, password, balance, phone_no, email);
    }

    @RequestMapping("/user/all")
    public String userGetAll(){
        List<User> users = userService.getAllUsers();
        return users.toString();
    }

    @RequestMapping("/user/details/{uid}")
    public User getUserDetails(@PathVariable Integer uid){
        return userService.getUser(uid);
    }
}

