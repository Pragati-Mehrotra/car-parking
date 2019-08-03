package com.alokbharti.parkme;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alokbharti.parkme.Interfaces.AuthInterface;
import com.alokbharti.parkme.Utilities.APIHelper;

import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;
import static com.alokbharti.parkme.Utilities.SavedSharedPreferences.getSignInStatus;
import static com.alokbharti.parkme.Utilities.SavedSharedPreferences.getUserId;
import static com.alokbharti.parkme.Utilities.SavedSharedPreferences.setUserId;

public class LandingPageActivity extends AppCompatActivity implements AuthInterface {

    private EditText userName;
    private EditText userPhoneNumber;
    private EditText userEmailId;
    private EditText userPassword;
    private LinearLayout signUpField;
    private Button submitButton;
    private TextView authTypeSwitch;
    private boolean signIn = true;

    private APIHelper apiHelper;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        initViews();

        progressDialog = new ProgressDialog(this);
        apiHelper = new APIHelper(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = userPhoneNumber.getText().toString();
                String password = userPassword.getText().toString();
                if(TextUtils.isEmpty(userPhoneNumber.getText().toString())){
                    userPhoneNumber.setError("This field is compulsory");
                    return;
                }
                if(TextUtils.isEmpty(userPassword.getText().toString())){
                    userPassword.setError("This field is compulsory");
                    return;
                }
                progressDialog.setMessage("Loading your details.....");
                progressDialog.show();
                if(signIn){
                    Log.e("signIn","call");
                    apiHelper.signInApiCall(phoneNumber, password);
                }else{
                    String name = userName.getText().toString();
                    String email = userEmailId.getText().toString();
                    if(TextUtils.isEmpty(userName.getText().toString())){
                        userPhoneNumber.setError("This field is compulsory");
                        return;
                    }
                    if(TextUtils.isEmpty(userEmailId.getText().toString())){
                        userPassword.setError("This field is compulsory");
                        return;
                    }
                    Log.e("signup","call");
                    apiHelper.signUpApiCall(name, password, phoneNumber, email);
                }
            }
        });

    }

    private void initViews(){
        userName = findViewById(R.id.user_name);
        userPassword = findViewById(R.id.user_password);
        userEmailId = findViewById(R.id.user_email);
        userPhoneNumber = findViewById(R.id.user_phone_number);
        signUpField = findViewById(R.id.sign_up_fields);
        submitButton = findViewById(R.id.submit_button);
        authTypeSwitch = findViewById(R.id.authTypeSwitch);
//        TextView signInTextView = findViewById(R.id.sign_in_tv);
//        TextView signUpTextView = findViewById(R.id.sign_up_tv);

        signUpField.setVisibility(View.GONE);
//        signInTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signIn = true;
//                signUpField.setVisibility(View.GONE);
//            }
//        });
//        signUpTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signIn = false;
//                signUpField.setVisibility(View.VISIBLE);
//            }
//        });

        authTypeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn = !signIn;
                if(signIn){
                    signUpField.setVisibility(View.GONE);
                    submitButton.setText("Login");
                    authTypeSwitch.setText("Not a user? Sign up");
                }
                else{
                    signUpField.setVisibility(View.VISIBLE);
                    submitButton.setText("Sign up");
                    authTypeSwitch.setText("Already registered? Log in");
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    public void onSignUpSuccessful(int userId) {
        progressDialog.dismiss();
        setUserId(this, userId, true);
        currentUserId = userId;
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onSignInSuccessful(int userId) {
        progressDialog.dismiss();
        setUserId(this, userId, true);
        currentUserId = userId;
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onAuthFailed() {
        progressDialog.dismiss();
        Toast.makeText(this, "Authentication failed, try again!!", Toast.LENGTH_SHORT).show();
    }
}
