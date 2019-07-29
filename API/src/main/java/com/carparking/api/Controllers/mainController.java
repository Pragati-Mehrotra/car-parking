package com.carparking.api.Controllers;

import com.carparking.api.Entity.Booking;
import com.carparking.api.Entity.History;
import com.carparking.api.Entity.Parking;
import com.carparking.api.Entity.User;
import com.carparking.api.Service.IParkingService;
import com.carparking.api.Service.IBookingService;
import com.carparking.api.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

// import com.carparking.api.Pojo.userLoginRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class mainController {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    IUserService userService;

    @Autowired
    IParkingService parkingService;

    @Autowired
    IBookingService bookingService;


    @RequestMapping("/")
    public String index() {
        return "<h1>ParkMe REST API</h1><br>" +
                "<code>Endpoints : <br><br>" +
                "1. user/login<br>" +
                "2. user/signUp<br>" +
                "3. user/details<br>" +
                "4. user/history<br>" +
                "<br>" +
                "5. booking/new<br>" +
                "6. booking/cancel<br>" +
                "7. booking/active<br>" +
                "8. booking/checkout<br>" +
                "<br>" +
                "9. parking/nearby<br>" +
                "10.parking/driveIn<br>" +
                "11.parking/driveOut<br>" +
                "12.parking/register<br></code><br>" +
                "Click <a href=\"https://docs.google.com/document/d/1kQsKVKUtZEoalN3GA3Fz9dC7pSiqy5O-MDac4izyvhk/edit?usp=sharing\">here</a> to get full API Documentation<br>";
    }

    //-------------------------------------------------User starts here-----------------------------------------------

    @RequestMapping(method = RequestMethod.POST ,value = "/user/login")
    public @ResponseBody
    User userLogin(@RequestBody String request) throws IOException{

        JsonNode reqNode = mapper.readTree(request);
        String phoneNo = reqNode.get("phoneNo").asText();
        String password = reqNode.get("password").asText();
        return userService.userLogin(phoneNo, password);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/signUp")
    public User userSignUp(@RequestBody User user) throws IOException{

        user = userService.saveUser(user);
        return user;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/email")
    public User addEmail(@RequestParam Integer userId, @RequestParam String email) throws IOException{
        return userService.saveUserEmail(userId, email);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/user/all")
    public List<User> userGetAll(){
        List<User> users = userService.getAllUsers();
        return users;
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/user/details")
    public User getUserDetails(@RequestParam Integer userId){
        return userService.getUser(userId);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/user/history")
    public List<History> getUserHistory(Integer userId){
        return userService.getUserHistory(userId);
    }

    //--------------------------------------Parking starts here --------------------------------------------------

    @RequestMapping(method = RequestMethod.POST ,value = "/parking/all")
    public @ResponseBody List<Parking> getAllParkings(){
        return parkingService.getParkings();
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/parking/nearby")
    public @ResponseBody List<Parking> getNearbyParkings(@RequestBody String request)throws IOException {

        JsonNode reqNode = mapper.readTree(request);
        Double latitude = reqNode.get("latitude").asDouble();
        Double longitude = reqNode.get("longitude").asDouble();
        Integer radius = reqNode.get("radius").asInt();

        return parkingService.getParkingsNearby(latitude, longitude, radius);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/parking/register")
    public @ResponseBody Parking registerParking(@RequestBody Parking parking)throws IOException {
        return parkingService.saveParking(parking);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/parking/driveIn")
    public @ResponseBody String driveIn(@RequestBody String request)throws IOException{

        JsonNode reqNode = mapper.readTree(request);
        Integer parkingId = reqNode.get("parkingId").asInt();
        Integer inOtp = reqNode.get("inOtp").asInt();

        return parkingService.driveIn(parkingId, inOtp);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/parking/driveOut")
    public @ResponseBody Object driveOut(@RequestBody String request)throws IOException{

        JsonNode reqNode = mapper.readTree(request);
        Integer parkingId = reqNode.get("parkingId").asInt();
        Integer outOtp = reqNode.get("outOtp").asInt();
        return parkingService.driveOut(parkingId, outOtp);
    }

    //--------------------------------------------- Booking starts here ----------------------------------------------

    @RequestMapping(method = RequestMethod.POST, value = "/booking/new")
    public @ResponseBody Booking registerBooking(@RequestBody Booking booking)throws IOException {
        return bookingService.saveBooking(booking);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/booking/details")
    public Object getBookingDetails(@RequestParam Integer bookingId){
        return bookingService.getBookingById(bookingId);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/booking/active")
    public List<Booking> getActiveBookings(@RequestParam Integer userId){
        return bookingService.getActiveBookings(userId);
    }


    @RequestMapping(method = RequestMethod.POST ,value = "/booking/checkout")
    public Booking getCheckoutOtp(@RequestParam Integer bookingId){
        return bookingService.checkout(bookingId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/booking/cancel")
    public History cancelBooking(@RequestParam Integer bookingId){
        return bookingService.cancelBooking(bookingId);
    }
    //--------------------------------------------- History starts here ----------------------------------------------
}

