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
import java.io.InputStream;
import org.springframework.util.StreamUtils;
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
    Object userLogin(@RequestBody String request) throws IOException{

        JsonNode reqNode = mapper.readTree(request);
        String phoneNo = reqNode.get("phoneNo").asText();
        String password = reqNode.get("password").asText();
        return userService.userLogin(phoneNo, password);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/signUp")
    public Object userSignUp(@RequestBody User user) throws IOException{
        return userService.saveUser(user);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/user/email")
    public Object addEmail(@RequestBody String request) throws IOException{
        JsonNode reqNode = mapper.readTree(request);
        Integer userId = reqNode.get("userId").asInt();
        String email = reqNode.get("email").asText();
        return userService.saveUserEmail(userId, email);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/user/all")
    public Object userGetAll(){
        return userService.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/user/details")
    public Object getUserDetails(@RequestBody String request) throws IOException{
        JsonNode reqNode = mapper.readTree(request);
        Integer userId = reqNode.get("userId").asInt();
        return userService.getUser(userId);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/user/history")
    public Object getUserHistory(@RequestBody String request) throws IOException{
        JsonNode reqNode = mapper.readTree(request);
        Integer userId = reqNode.get("userId").asInt();
        return userService.getUserHistory(userId);
    }

    //--------------------------------------Parking starts here --------------------------------------------------

    @RequestMapping(method = RequestMethod.POST ,value = "/parking/all")
    public @ResponseBody List<Parking> getAllParkings(){
        return parkingService.getParkings();
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/parking/nearby")
    public @ResponseBody List<Parking> getNearbyParkings(@RequestBody String request) throws IOException {

        JsonNode reqNode = mapper.readTree(request);
        Double latitude = reqNode.get("latitude").asDouble();
        Double longitude = reqNode.get("longitude").asDouble();
        Integer radius = reqNode.get("radius").asInt();

        return parkingService.getParkingsNearby(latitude, longitude, radius);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/parking/register")
    public @ResponseBody Parking registerParking(@RequestBody Parking parking) throws IOException {
        return parkingService.saveParking(parking);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/parking/details")
    public Object getParkingDetails(@RequestBody String request) throws IOException{
        JsonNode reqNode = mapper.readTree(request);
        Integer parkingId = reqNode.get("parkingId").asInt();
        return parkingService.getParkingById(parkingId);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/parking/driveIn")
    public @ResponseBody String driveIn(@RequestBody String request) throws IOException{

        JsonNode reqNode = mapper.readTree(request);
        Integer parkingId = reqNode.get("parkingId").asInt();
        Integer inOtp = reqNode.get("inOtp").asInt();

        return parkingService.driveIn(parkingId, inOtp);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/parking/driveOut")
    public @ResponseBody Object driveOut(@RequestBody String request) throws IOException{

        JsonNode reqNode = mapper.readTree(request);
        Integer parkingId = reqNode.get("parkingId").asInt();
        Integer outOtp = reqNode.get("outOtp").asInt();
        return parkingService.driveOut(parkingId, outOtp);
    }

    //--------------------------------------------- Booking starts here ----------------------------------------------

    @RequestMapping(method = RequestMethod.POST, value = "/booking/new")
    public @ResponseBody Booking registerBooking(@RequestBody Booking booking) throws IOException {
        return bookingService.saveBooking(booking);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/booking/details")
    public Object getBookingDetails(@RequestBody String request) throws IOException{
        JsonNode reqNode = mapper.readTree(request);
        Integer bookingId = reqNode.get("bookingId").asInt();
        return bookingService.getBookingById(bookingId);
    }

    @RequestMapping(method = RequestMethod.POST ,value = "/booking/active")
    public Object getActiveBookings(@RequestBody String request) throws IOException{
        JsonNode reqNode = mapper.readTree(request);
        Integer userId = reqNode.get("userId").asInt();
        return bookingService.getActiveBookings(userId);
    }


    @RequestMapping(method = RequestMethod.POST ,value = "/booking/checkout")
    public Booking getCheckoutOtp(@RequestBody String request) throws IOException{
        JsonNode reqNode = mapper.readTree(request);
        Integer bookingId = reqNode.get("bookingId").asInt();
        return bookingService.checkout(bookingId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/booking/cancel")
    public History cancelBooking(@RequestBody String request) throws IOException{
        JsonNode reqNode = mapper.readTree(request);
        Integer bookingId = reqNode.get("bookingId").asInt();
        return bookingService.cancelBooking(bookingId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/booking/bill")
    public List<Object> calculateBill(@RequestBody String request) throws IOException{
        JsonNode reqNode = mapper.readTree(request);
        Double slotDuration = reqNode.get("slotDuration").asDouble();
        return bookingService.calculateBill(slotDuration);
    }
    //--------------------------------------------- History starts here ----------------------------------------------

    @RequestMapping(method = RequestMethod.GET ,value = "/parking/driveIn")
    public String driveIn() {

        return "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0, maximum-scale=1.0\" />\n" +
                "    <title>ParkMe</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n" +
                "    <style>@media screen and (max-device-width:480px){body{-webkit-text-size-adjust:none}}</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div>\n" +
                "    <div style=\"text-align:center; margin-top: 20px;\">\n" +
                "        <img src=\"https://github.com/jainsomya972/car-parking/blob/master/API/src/assets/logo.png?raw=true\">\n" +
                "    </div> \n" +
                "    <h1 style=\"text-align:center; margin: 0px\">Welcome to ParkMe</h1>\n" +
                "    <h6 style=\"text-align:center; margin: 40px;\">Please enter the Parking Id and Entry OTP to open the gate.</h6>\n" +
                "    <div style=\"display: flex; justify-content: center; margin: 20px;\">\n" +
                "      <label>Enter Parking Id:  </label>\n" +
                "      <input id=\"parkingId\" class=\"form-control\" type=\"text\" aria-describedby=\"inputGroup-sizing-sm\" style=\"height:30px;width:300px; margin-left:30px\">\n" +
                "    </div>\n" +
                "    <div style=\"display: flex; justify-content: center; margin: 20px;\">\n" +
                "      <label>Enter Entry OTP:  </label>\n" +
                "      <input id=\"inOtp\" class=\"form-control\" type=\"text\" aria-describedby=\"inputGroup-sizing-sm\" style=\"height:30px;width:300px; margin-left:30px\">\n" +
                "    </div>\n" +
                "    <div style=\"display: flex; justify-content: center; margin: 20px;\">\n" +
                "      <button type=\"button\" class=\"btn btn-outline-primary\" onclick=\"loadDoc()\">Submit</button>\n" +
                "    </div>\n" +
                "    <p id=\"response\" style=\"display: flex; justify-content: center; margin: 20px; color:red\"></p>\n" +
                "  </div>\n" +
                "  <script>\n" +
                "      function loadDoc() {\n" +
                "        var request = {\n" +
                "          parkingId: document.getElementById(\"parkingId\").value,\n" +
                "          inOtp:document.getElementById(\"inOtp\").value\n" +
                "        }\n" +
                "        if (request.parkingId == '' || request.inOtp == '') {\n" +
                "          document.getElementById(\"response\").innerHTML = \"Please fill both the fields.\"\n" +
                "        }\n" +
                "        else {\n" +
                "          var xhttp = new XMLHttpRequest();\n" +
                "          xhttp.onreadystatechange = function() {\n" +
                "            if (this.readyState == 4 && this.status == 200) {\n" +
                "              document.getElementById(\"response\").innerHTML = this.responseText;\n" +
                "              document.getElementById(\"parkingId\").value = '';\n" +
                "              document.getElementById(\"inOtp\").value = '';\n" +
                "            }\n" +
                "          }\n" +
                "          xhttp.open(\"POST\", \"http://52.172.30.204:5433/parking/driveIn\", true);\n" +
                "          xhttp.setRequestHeader(\"Content-type\", \"application/json\");\n" +
                "          xhttp.send(JSON.stringify(request));\n" +
                "        }\n" +
                "      }\n" +
                "      </script>\n" +
                "</body>\n" +
                "</html>";
    }



    @RequestMapping(method = RequestMethod.GET ,value = "/parking/driveOut")
    public String driveOut() {

        return "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width,minimum-scale=1.0, maximum-scale=1.0\" />\n" +
                "    <title>ParkMe</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n" +
                "    <style>@media screen and (max-device-width:480px){body{-webkit-text-size-adjust:none}}</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div>\n" +
                "    <div style=\"text-align:center; margin-top: 20px;\">\n" +
                "        <img src=\"https://github.com/jainsomya972/car-parking/blob/master/API/src/assets/logo.png?raw=true\">\n" +
                "    </div> \n" +
                "    <h1 style=\"text-align:center\">Welcome to ParkMe</h1>\n" +
                "    <h6 style=\"text-align:center; margin: 40px;\">Please enter the Parking Id and Exit OTP to open the gate.</h6>\n" +
                "    <div style=\"display: flex; justify-content: center; margin: 20px;\">\n" +
                "      <label>Enter Parking Id:  </label>\n" +
                "      <input id=\"parkingId\" class=\"form-control\" type=\"text\" aria-describedby=\"inputGroup-sizing-sm\" style=\"height:30px;width:300px; margin-left:30px\">\n" +
                "    </div>\n" +
                "    <div style=\"display: flex; justify-content: center; margin: 20px;\">\n" +
                "      <label>Enter Exit OTP:  </label>\n" +
                "      <input id=\"outOtp\" class=\"form-control\" type=\"text\" aria-describedby=\"inputGroup-sizing-sm\" style=\"height:30px;width:300px; margin-left:30px\">\n" +
                "    </div>\n" +
                "    <div style=\"display: flex; justify-content: center; margin: 20px;\">\n" +
                "      <button type=\"button\" class=\"btn btn-outline-primary\" onclick=\"loadDoc()\">Submit</button>\n" +
                "    </div>\n" +
                "    <p id=\"response\" style=\"display: flex; justify-content: center; margin: 20px; color:red\"></p>\n" +
                "  </div>\n" +
                "  <script>\n" +
                "      function loadDoc() {\n" +
                "        var request = {\n" +
                "          parkingId: document.getElementById(\"parkingId\").value,\n" +
                "          outOtp:document.getElementById(\"outOtp\").value\n" +
                "        }\n" +
                "        if (request.parkingId == '' || request.outOtp == '') {\n" +
                "          document.getElementById(\"response\").innerHTML = \"Please fill both the fields.\"\n" +
                "        }\n" +
                "        else {\n" +
                "          var xhttp = new XMLHttpRequest();\n" +
                "          xhttp.onreadystatechange = function() {\n" +
                "            if (this.readyState == 4 && this.status == 200) {\n" +
                "              document.getElementById(\"response\").innerHTML = this.responseText;\n" +
                "              document.getElementById(\"parkingId\").value = '';\n" +
                "              document.getElementById(\"outOtp\").value = '';\n" +
                "            }\n" +
                "          }\n" +
                "          xhttp.open(\"POST\", \"http://52.172.30.204:5433/parking/driveOut\", true);\n" +
                "          xhttp.setRequestHeader(\"Content-type\", \"application/json\");\n" +
                "          xhttp.send(JSON.stringify(request));\n" +
                "        }\n" +
                "      }\n" +
                "      </script>\n" +
                "</body>\n" +
                "</html>";
    }


//    @RequestMapping(method = RequestMethod.GET, value = "/scheduler")
//    public void scheduler(){
//         parkingService.scheduleCall();
//    }
}

