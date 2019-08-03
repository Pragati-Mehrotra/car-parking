package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.alokbharti.parkme.Interfaces.ProfileInterface;
import com.alokbharti.parkme.Pojo.UserInfo;
import com.alokbharti.parkme.Utilities.APIHelper;

import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;

public class ProfileActivity extends AppCompatActivity implements ProfileInterface {


    private TextView userName, userEmail, userBalance, userPhoneNo;
    private APIHelper apiHelper;

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
    }

    public void initViews(){
        userName = findViewById(R.id.profile_name);
        userBalance = findViewById(R.id.profile_balance);
        userEmail = findViewById(R.id.profile_email);
        userPhoneNo = findViewById(R.id.profile_phone_no);
    }

    @Override
    public void onGetProfileInfo(UserInfo userInfo) {
        userName.setText(userInfo.getName());
        userBalance.setText(userInfo.getBalance().toString());
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
