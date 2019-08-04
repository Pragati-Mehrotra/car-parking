package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alokbharti.parkme.Interfaces.ProfileInterface;
import com.alokbharti.parkme.Pojo.UserInfo;
import com.alokbharti.parkme.Utilities.APIHelper;

import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;
import static com.alokbharti.parkme.Utilities.SavedSharedPreferences.setUserId;

public class ProfileActivity extends AppCompatActivity implements ProfileInterface {


    private TextView userName, userEmail, userBalance, userPhoneNo;
    private APIHelper apiHelper;
    private Button signoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("My Profile");

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();

        apiHelper = new APIHelper(this);
        apiHelper.getUserDetails(currentUserId);
        System.out.println("---------------------------------------------------->>> UserId : " + currentUserId);

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserId(ProfileActivity.this, currentUserId, false);
                startActivity(new Intent(ProfileActivity.this, LandingPageActivity.class));
            }
        });
    }

    public void initViews(){
        userName = findViewById(R.id.profile_name);
        userBalance = findViewById(R.id.profile_balance);
        userEmail = findViewById(R.id.profile_email);
        userPhoneNo = findViewById(R.id.profile_phone_no);
        signoutButton = findViewById(R.id.signoutButton);
    }

    @Override
    public void onGetProfileInfo(UserInfo userInfo) {
        userName.setText(userInfo.getName());
        userBalance.setText("₹" + userInfo.getBalance().toString());
        userPhoneNo.setText(userInfo.getPhoneNo());
        userEmail.setText(userInfo.getEmail());
    }

    @Override
    public void onGetProfileInfoFailed() {
        Toast.makeText(this,"Error fetching user", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
