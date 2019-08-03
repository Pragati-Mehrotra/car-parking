package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alokbharti.parkme.Interfaces.CommonAPIInterface;
import com.alokbharti.parkme.Utilities.APIHelper;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.alokbharti.parkme.Utilities.GlobalConstants.cancelBookingUrl;
import static com.alokbharti.parkme.Utilities.GlobalConstants.checkoutBookingUrl;
import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;
import static com.alokbharti.parkme.Utilities.GlobalConstants.parkingDetailsUrl;

public class ActiveBooking extends AppCompatActivity implements CommonAPIInterface {

    private TextView noActiveBookingTV;
    private TextView parkingAddress;
    private TextView bill;
    private TextView bookingTimeStamp;
    private TextView bookingSlotDuration;
    private TextView activeBookingId;
    private TextView bookingStatus;
    private TextView inOtp;
    private TextView outOtp;
    private Button checkout;
    private APIHelper apiHelper;
    private LinearLayout activeBookingsDetails;
    private Button cancelActiveBooking;

    boolean isActiveBookings = false;

    private int bookingId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_booking);
        setTitle("My Active Booking");

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initViews();
        apiHelper = new APIHelper(this);
        apiHelper.getActiveBookingDetails(currentUserId);
    }

    private void initViews() {
        activeBookingId = findViewById(R.id.active_booking_id);
        noActiveBookingTV = findViewById(R.id.no_active_booking_tv);
        parkingAddress = findViewById(R.id.active_booking_parking_address);
        inOtp = findViewById(R.id.active_booking_in_otp);
        outOtp = findViewById(R.id.active_booking_out_otp);
        bill = findViewById(R.id.active_booking_bill);
        bookingSlotDuration = findViewById(R.id.active_booking_slot_duration);
        bookingTimeStamp = findViewById(R.id.active_booking_in_time);
        bookingStatus = findViewById(R.id.active_booking_status);
        checkout = findViewById(R.id.checkout);
        activeBookingsDetails = findViewById(R.id.active_booking_details);
        cancelActiveBooking = findViewById(R.id.cancel_active_booking);

        cancelActiveBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ActiveBooking.this)
                        .setTitle("CANCEL BOOKING")
                        .setMessage("Are you sure wanna cancel this booking??")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cancelBookingApiCall(bookingId);
                            }
                        })
                        .setNegativeButton("CANCEL", null)
                        .show();
            }
        });
    }

    @Override
    public void onSuccessfulHit(final JSONObject response) {
        if(response==null){
            //empty body
            isActiveBookings = false;
            noActiveBookingTV.setVisibility(View.VISIBLE);
            activeBookingsDetails.setVisibility(View.GONE);
        }else{
            isActiveBookings = true;
            noActiveBookingTV.setVisibility(View.GONE);
            activeBookingsDetails.setVisibility(View.VISIBLE);

            try {
                bookingId = response.getInt("bookingId");
                activeBookingId.setText(String.format(Locale.ENGLISH,"Booking ID: %d", bookingId));
                int parkingId = response.getInt("parkingId");
                getParkingDetails(parkingId);
                bill.setText(String.format("Total Bill: %s", String.valueOf(response.getDouble("bill"))));
                bookingSlotDuration.setText(String.format("Booking slot duration: %s", String.valueOf(response.getInt("slotDuration"))));
                inOtp.setText(String.format("inOtp: %d",response.getInt("inOtp")));

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                long inTimeStamp = response.getLong("inTime");
                Log.e("inTimeStamp in AB: ", " "+inTimeStamp);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(inTimeStamp);
                bookingTimeStamp.setText(String.format("Booking time: %s", formatter.format(calendar.getTime())));
                String status = response.getString("status");
                bookingStatus.setText(String.format("Booking status: %s", status));
                if(status.equals("Booked")){
                    cancelActiveBooking.setEnabled(true);
                }else{
                    cancelActiveBooking.setEnabled(false);
                }
                if(status.equals("CheckedOut")){
                    inOtp.setVisibility(View.GONE);
                    outOtp.setVisibility(View.VISIBLE);
                    outOtp.setText(String.format(Locale.ENGLISH,"Out OTP: %d", response.getInt("outOtp")));
                }
                if(status.equals("Booked") || status.equals("CheckedOut")){
                    checkout.setEnabled(false);
                }
                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            getOutOtp(response.getInt("bookingId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getOutOtp(int bookingId) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting OTP.....");
        progressDialog.show();

        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("bookingId", bookingId);
        }catch(JSONException e){
            e.printStackTrace();
        }

        Rx2AndroidNetworking.post(checkoutBookingUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            View view = LayoutInflater.from(ActiveBooking.this).inflate(R.layout.otp_dialog_alert, null);
                            final AlertDialog alertDialog = new AlertDialog.Builder(ActiveBooking.this)
                                    .setView(view)
                                    .show();

                            TextView checkoutOtpTV = view.findViewById(R.id.checkout_otp);
                            checkoutOtpTV.setText(String.valueOf(response.getInt("outOtp")));
                            AppCompatButton cancelButton = view.findViewById(R.id.checkout_cancel_button);
                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });

                            AppCompatButton checkoutProceedButton = view.findViewById(R.id.checkout_proceed_button);
                            checkoutProceedButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(ActiveBooking.this, "Your Booking status will be updated. Thanks for booking with us :)", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    apiHelper.getActiveBookingDetails(currentUserId);

                                    setTimerForBookingStatus();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(ActiveBooking.this, "Can't able to get OTP!!!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setTimerForBookingStatus() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                apiHelper.getActiveBookingDetails(currentUserId);
            }
        },0, 5000);
    }

    @Override
    public void onFailureAPIHit() {
        Toast.makeText(this, "Failed to get data, check our internet", Toast.LENGTH_SHORT).show();
    }

    public void getParkingDetails(int parkingId){
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("parkingId", parkingId);
        }catch(JSONException e){
            e.printStackTrace();
        }
        Log.e("parking Id: ", ""+ parkingId);
        Rx2AndroidNetworking.post(parkingDetailsUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("parking address", response.getString("address"));
                            parkingAddress.setText(String.format("Parking address: %s", response.getString("address")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public void cancelBookingApiCall(int bookingId){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("bookingId", bookingId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Rx2AndroidNetworking.post(cancelBookingUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ActiveBooking.this, "Booking cancelled", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActiveBooking.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!isActiveBookings){
            finish();
        }else {
            finishAffinity();
        }
    }
}
