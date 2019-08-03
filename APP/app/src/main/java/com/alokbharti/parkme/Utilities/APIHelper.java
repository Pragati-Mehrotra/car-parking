package com.alokbharti.parkme.Utilities;

import android.util.Log;

import com.alokbharti.parkme.Interfaces.AuthInterface;
import com.alokbharti.parkme.Interfaces.CommonAPIInterface;
import com.alokbharti.parkme.Interfaces.HistoryInterface;
import com.alokbharti.parkme.Interfaces.JSONArrayAPIInterface;
import com.alokbharti.parkme.Interfaces.LocationInterface;
import com.alokbharti.parkme.Interfaces.ProfileInterface;
import com.alokbharti.parkme.Pojo.History;
import com.alokbharti.parkme.Pojo.ParkingInfo;
import com.alokbharti.parkme.Pojo.UserInfo;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.alokbharti.parkme.Utilities.GlobalConstants.*;

public class APIHelper {

    private AuthInterface authInterface;
    private LocationInterface locationInterface;
    private CommonAPIInterface commonAPIInterface;
    private ProfileInterface profileInterface;
    private HistoryInterface historyInterface;
    private JSONArrayAPIInterface jsonArrayAPIInterface;

    public APIHelper(JSONArrayAPIInterface jsonArrayAPIInterface) {
        this.jsonArrayAPIInterface = jsonArrayAPIInterface;
    }

    public APIHelper(AuthInterface authInterface) {
        this.authInterface = authInterface;
    }

    public APIHelper(LocationInterface locationInterface){
        this.locationInterface = locationInterface;
    }

    public APIHelper(CommonAPIInterface commonAPIInterface){
        this.commonAPIInterface = commonAPIInterface;
    }

    public APIHelper(ProfileInterface profileInterface){
        this.profileInterface = profileInterface;
    }

    public APIHelper(HistoryInterface historyInterface){
        this.historyInterface = historyInterface;
    }

    public void signInApiCall(String phoneNumber, String password){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phoneNo", phoneNumber);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Rx2AndroidNetworking.post(loginUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int userId = 0;
                        try {
                            userId = response.getInt("userId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        authInterface.onSignInSuccessful(userId);
                    }

                    @Override
                    public void onError(ANError anError) {
                        //Log.e("signin error", anError.getMessage());
                        authInterface.onAuthFailed();
                    }
                });

    }

    public void signUpApiCall(String userName, String password, String phoneNumber, String emailId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", userName);
            jsonObject.put("password", password);
            jsonObject.put("phoneNo", phoneNumber);
            jsonObject.put("email", emailId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Rx2AndroidNetworking.post(signUpUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int userId = 0;
                        try {
                            userId = response.getInt("userId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        authInterface.onSignUpSuccessful(userId);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("signUp error", anError.getMessage());
                        authInterface.onAuthFailed();
                    }
                });
    }

    public void getParkingNearby(double lat, double lon){
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", lat);
            jsonObject.put("longitude", lon);
            jsonObject.put("radius",5000);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Rx2AndroidNetworking.post(nearbyParkingUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<ParkingInfo> parkingList = new ArrayList<>();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                ParkingInfo parkingInfo = new ParkingInfo(object.getInt("parkingId"),
                                        object.getString("parkingName"),
                                        object.getString("address"),
                                        object.getLong("latitude"),
                                        object.getLong("longitude"),
                                        object.getInt("totalSlots"),
                                        object.getInt("availableSlots"));

                                parkingList.add(parkingInfo);
                            }

                            Log.e("Got list successful",":)");
                            locationInterface.onGetParkingList(parkingList);
                        }catch (JSONException e){
                            Log.e("Failed to get data", ":(");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        locationInterface.onFailedGettingparkingList();
                    }
                });
    }

    public void getNewBookingDetails(int userId, int parkingId, int slotDuration){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("userId", userId);
            jsonObject.put("parkingId", parkingId);
            jsonObject.put("slotDuration", slotDuration);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("sending", jsonObject.toString());

        Rx2AndroidNetworking.post(newBookingUrl)
        .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        commonAPIInterface.onSuccessfulHit(response);
                        }

                    @Override
                    public void onError(ANError anError) {
                        //Log.e("new booking", anError.getMessage());
                        commonAPIInterface.onFailureAPIHit();
                    }
                });
    }

    public void getUserDetails(int userId){
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("userId", userId);
        }catch(JSONException e){
            e.printStackTrace();
        }

        Rx2AndroidNetworking.post(userDetailsUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UserInfo userInfo = new UserInfo(response.getString("name"),
                                                            response.getInt("userId"),
                                                            response.getString("password"),
                                                            response.getInt("balance"),
                                                            response.getString("phoneNo"),
                                                            response.getString("email"));
                            profileInterface.onGetProfileInfo(userInfo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println("----------------------------------------------->>>" + anError.getErrorBody());
                        profileInterface.onGetProfileInfoFailed();
                    }
                });
    }

    public void getUserHistory(int userId){
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("userId", userId);
        }catch(JSONException e){
            e.printStackTrace();
        }

        Rx2AndroidNetworking.post(userHistoryUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<History> historyList = new ArrayList<>();
                        for(int i =0; i<response.length();i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                History history = new History();
                                history.setBookingId(object.getInt("bookingId"));
                                if(object.has("bill") && !object.isNull("bill"))
                                    history.setBill(object.getDouble("bill"));
                                history.setInTime(object.getLong("inTime"));
                                if(object.has("outTime") && !object.isNull("outTime"))
                                    history.setOutTime(object.getLong("outTime"));
                                history.setParkingId(object.getInt("parkingId"));
                                history.setSlotDuration(object.getInt("slotDuration"));
                                history.setParkingName(object.getString("parkingName"));
                                history.setStatus(object.getString("status"));
                                history.setUserId(object.getInt("userId"));
                                historyList.add(history);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        historyInterface.onGetHistorySuccess(historyList);
                        System.out.println("------------------------------------------->>>" + historyList);
                    }

                    @Override
                    public void onError(ANError anError) {
                        System.out.println(anError.getErrorBody());
                        historyInterface.onGetHistoryFailed();

                    }
                });
    }

    public void getActiveBookingDetails(int userId){
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("userId", userId);
        }catch(JSONException e){
            e.printStackTrace();
        }
        Log.e("userId", jsonObject.toString());

        Rx2AndroidNetworking.post(activeBookingUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.e("in active bd", response.toString());
                            if(response.length()==0){
                                commonAPIInterface.onSuccessfulHit(null);
                            }else {
                                commonAPIInterface.onSuccessfulHit(response.getJSONObject(0));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        commonAPIInterface.onFailureAPIHit();
                    }
                });
    }

    public void getBillDetails(int slotDuration){
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("slotDuration", slotDuration);
        }catch(JSONException e){
            e.printStackTrace();
        }
        Log.e("userId", jsonObject.toString());

        Rx2AndroidNetworking.post(bookingBillUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        jsonArrayAPIInterface.onSuccessfulHit(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        jsonArrayAPIInterface.onFailureAPIHit();
                    }
                });
    }
}