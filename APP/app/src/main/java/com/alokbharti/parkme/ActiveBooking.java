package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
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

import static com.alokbharti.parkme.Utilities.GlobalConstants.checkoutBookingUrl;
import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;
import static com.alokbharti.parkme.Utilities.GlobalConstants.parkingDetailsUrl;

public class ActiveBooking extends AppCompatActivity implements CommonAPIInterface {

    private TextView noActiveBookingTV;
    private TextView parkingAddress;
    private TextView bill;
    private TextView bookingTimeStamp;
    private TextView bookingSlotDuration;
    private TextView bookingStatus;
    private Button checkout;
    private APIHelper apiHelper;
    private LinearLayout activeBookingsDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_booking);

        initViews();
        apiHelper = new APIHelper(this);
        apiHelper.getActiveBookingDetails(currentUserId);
    }

    private void initViews() {
        noActiveBookingTV = findViewById(R.id.no_active_booking_tv);
        parkingAddress = findViewById(R.id.active_booking_parking_address);
        bill = findViewById(R.id.active_booking_bill);
        bookingSlotDuration = findViewById(R.id.active_booking_slot_duration);
        bookingTimeStamp = findViewById(R.id.active_booking_in_time);
        bookingStatus = findViewById(R.id.active_booking_status);
        checkout = findViewById(R.id.checkout);
        activeBookingsDetails = findViewById(R.id.active_booking_details);
    }

    @Override
    public void onSuccessfulHit(final JSONObject response) {
        if(response.length()==0){
            //empty body
            noActiveBookingTV.setVisibility(View.VISIBLE);
            activeBookingsDetails.setVisibility(View.GONE);
        }else{
            noActiveBookingTV.setVisibility(View.GONE);
            activeBookingsDetails.setVisibility(View.VISIBLE);

            try {
                int parkingId = response.getInt("parkingId");
                getParkingDetails(parkingId);
                bill.setText(String.format("Total Bill: %s", String.valueOf(response.getDouble("bill"))));
                bookingSlotDuration.setText(String.format("Booking slot duration: %s", String.valueOf(response.getInt("slotDuration"))));

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                long inTimeStamp = response.getLong("inTime");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(inTimeStamp);
                bookingTimeStamp.setText(String.format("Booking time: %s", formatter.format(calendar)));
                bookingStatus.setText(String.format("Booking status: %s", response.getString("status")));
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
            jsonObject.put("userId", bookingId);
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
                            new AlertDialog.Builder(ActiveBooking.this)
                                    .setMessage("Please enter this otp to checkout: "+response.getInt("outOtp"))
                                    //TODO: set a button which allow users to proceed
                                    .show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

    @Override
    public void onFailureAPIHit() {
        Toast.makeText(this, "Failed to get data, check our internet", Toast.LENGTH_SHORT).show();
    }

    public void getParkingDetails(int parkingId){
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("userId", parkingId);
        }catch(JSONException e){
            e.printStackTrace();
        }

        Rx2AndroidNetworking.post(parkingDetailsUrl)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            parkingAddress.setText(String.format("Parking address: %s", response.getString("parkingAddress")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
