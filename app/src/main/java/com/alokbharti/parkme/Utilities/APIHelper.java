package com.alokbharti.parkme.Utilities;

import com.alokbharti.parkme.Interfaces.AuthInterface;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import static com.alokbharti.parkme.Utilities.GlobalConstants.loginUrl;
import static com.alokbharti.parkme.Utilities.GlobalConstants.signUpUrl;

public class APIHelper {

    private AuthInterface authInterface;

    public APIHelper(AuthInterface authInterface) {
        this.authInterface = authInterface;
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
            jsonObject.put("password", password);
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
                        authInterface.onAuthFailed();
                    }
                });
    }
}