package com.alokbharti.parkme.Utilities;

import android.util.Log;

import com.alokbharti.parkme.Interfaces.AuthInterface;
import com.alokbharti.parkme.Interfaces.LocationInterface;
import com.alokbharti.parkme.ParkingInfo;
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

    public APIHelper(AuthInterface authInterface) {
        this.authInterface = authInterface;
    }

    public APIHelper(LocationInterface locationInterface){
        this.locationInterface = locationInterface;
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
                        Log.e("signin error", anError.getMessage());
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
                        for (int i=0; i<response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                ParkingInfo parkingInfo = new ParkingInfo(object.getInt("parkingId"),
                                        object.getString("parkingName"),
                                        object.getString("address"),
                                        object.getLong("latitude"),
                                        object.getLong("longitude"),
                                        object.getInt("totalSlots"),
                                        object.getInt("availableSlots"));

                                parkingList.add(parkingInfo);
                                locationInterface.onGetParkingList(parkingList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        locationInterface.onFailedGettingparkingList();
                    }
                });
    }
}