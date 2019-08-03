package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alokbharti.parkme.Interfaces.CommonAPIInterface;
import com.alokbharti.parkme.Utilities.APIHelper;

import org.json.JSONObject;

import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;
import static com.alokbharti.parkme.Utilities.SavedSharedPreferences.getSignInStatus;
import static com.alokbharti.parkme.Utilities.SavedSharedPreferences.getUserId;

public class SplashScreenActivity extends AppCompatActivity implements CommonAPIInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentUserId = getUserId(this);
        if(currentUserId == 0){
            Log.e("userId", "0");
            startActivity(new Intent(this, LandingPageActivity.class));
        }
        if(getSignInStatus(this)){
            Log.e("user", "loggedIn");
            new APIHelper(this).getActiveBookingDetails(currentUserId);
        }else{
            Log.e("user", "not signed in");
            startActivity(new Intent(this, LandingPageActivity.class));
        }
    }

    @Override
    public void onSuccessfulHit(JSONObject response) {
        Log.e("in success","ss");
        if(response==null){
            startActivity(new Intent(this, MainActivity.class));
        }else{
            startActivity(new Intent(this, ActiveBooking.class));
        }
    }

    @Override
    public void onFailureAPIHit() {
        Toast.makeText(this, "Failed to get login details", Toast.LENGTH_SHORT).show();
    }
}
