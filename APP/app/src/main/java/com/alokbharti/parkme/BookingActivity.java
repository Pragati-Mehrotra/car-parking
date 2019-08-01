package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static com.alokbharti.parkme.Utilities.GlobalConstants.cancelBookingUrl;
import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;

public class BookingActivity extends AppCompatActivity implements CommonAPIInterface {

    EditText bookingSlotDuration;
    Button bookingButton;
    private APIHelper apiHelper;
    private String parkingAddress;
    private LinearLayout bookingLinearLayout;

    private TextView bookingId;
    private TextView parkingAddressTv;
    private TextView bill;
    private TextView slotDuration;
    private TextView inTime;
    private TextView outTime;
    private TextView inOtp;
    private TextView outOtp;
    private TextView boookingStatus;
    private Button payBillButton;
    private Button cancelBookingButton;
    private int newBookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setTitle("Book Parking Slots");
        initViews();

        apiHelper = new APIHelper(this);

        final int parkingId = getIntent().getIntExtra("parking_id", 0);
        parkingAddress = getIntent().getStringExtra("parking_address");

        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slotDuration = bookingSlotDuration.getText().toString();
                if(TextUtils.isEmpty(slotDuration)){
                    bookingSlotDuration.setError("This is required");
                    return;
                }
                apiHelper.getBookingDetails(currentUserId, parkingId, Integer.parseInt(slotDuration), 0);
            }
        });
    }

    private void initViews() {
        bookingLinearLayout = findViewById(R.id.booking_ll);
        bookingSlotDuration = findViewById(R.id.booking_slot_duration);
        bookingButton = findViewById(R.id.booking_button);
        bookingId = findViewById(R.id.booking_id);
        parkingAddressTv = findViewById(R.id.parking_address);
        bill = findViewById(R.id.bill);
        slotDuration = findViewById(R.id.slot_duration);
        inTime = findViewById(R.id.in_time);
        outTime = findViewById(R.id.out_time);
        inOtp = findViewById(R.id.in_otp);
        outOtp = findViewById(R.id.out_otp);
        boookingStatus = findViewById(R.id.booking_status);
        payBillButton = findViewById(R.id.pay_bill_button);
        payBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
                intent.putExtra("amount",bill.getText().toString());
                startActivity(intent);
            }
        });

        cancelBookingButton = findViewById(R.id.cancel_booking);
        cancelBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBookingApiCall(newBookingId);
            }
        });
    }

    @Override
    public void onSuccessfulHit(JSONObject response) {
        Log.e("response: ",response.toString());
        bookingLinearLayout.setVisibility(View.GONE);
        cancelBookingButton.setVisibility(View.VISIBLE);
        payBillButton.setVisibility(View.VISIBLE);
        parkingAddressTv.setText(String.format("Parking Address: %s", parkingAddress));
        try {
            newBookingId = response.getInt("bookingId");
            bookingId.setText(String.format(Locale.ENGLISH,"Booking Id: %d", newBookingId));
            bill.setText(String.format("Bill: %s", response.getDouble("bill")));
            slotDuration.setText(String.format(Locale.ENGLISH,"SlotDuration: %d", response.getInt("slotDuration")));
            inOtp.setText(String.format(Locale.ENGLISH,"In OTP: %d", response.getInt("inOtp")));
            boookingStatus.setText(String.format("Status: %s", response.getString("status")));

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            long inTimeStamp = response.getLong("inTime");
            Log.e("inTimeStamp", ": "+inTimeStamp);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(inTimeStamp);
            Log.e("TImeStamp: ",formatter.format(calendar.getTime()));
            inTime.setText("In timeStamp: "+formatter.format(calendar.getTime()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureAPIHit() {
        Toast.makeText(this, "Failed to get data!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BookingActivity.this, "Booking cancelled", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
