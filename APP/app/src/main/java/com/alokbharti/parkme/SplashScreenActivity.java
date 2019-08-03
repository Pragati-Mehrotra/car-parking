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

public class SplashScreenActivity extends AppCompatActivity implements CommonAPIInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if(currentUserId == 0){
            startActivity(new Intent(this, LandingPageActivity.class));
        }
        if(getSignInStatus(this)){
            new APIHelper(this).getActiveBookingDetails(currentUserId);
        }else{
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
