package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alokbharti.parkme.Interfaces.CommonAPIInterface;
import com.alokbharti.parkme.Utilities.APIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;

public class BookingActivity extends AppCompatActivity implements CommonAPIInterface {

    EditText bookingSlotDuration;
    Button bookingButton;
    private APIHelper apiHelper;
    private String parkingAddress;

    private TextView bookingId;
    private TextView parkingAddressTv;
    private TextView bill;
    private TextView timestamp;
    private TextView slotDuration;
    private TextView inTime;
    private TextView outTime;
    private TextView inOtp;
    private TextView outOtp;
    private TextView boookingStatus;
    private Button payBillButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

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
        bookingSlotDuration = findViewById(R.id.booking_slot_duration);
        bookingButton = findViewById(R.id.booking_button);
        bookingId = findViewById(R.id.booking_id);
        parkingAddressTv = findViewById(R.id.parking_address);
        bill = findViewById(R.id.bill);
        timestamp = findViewById(R.id.timestamp);
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

            }
        });
    }

    @Override
    public void onSuccessfulHit(JSONObject response) {
        Log.e("response: ",response.toString());
        parkingAddressTv.setText("Parking Address: "+parkingAddress);
        try {
            bookingId.setText("Booking Id: "+response.getInt("bookingId"));
            bill.setText("Bill: "+response.getDouble("bill"));
            slotDuration.setText("SlotDuration: "+response.getInt("slotDuration"));
            inOtp.setText("In OTP: "+response.getInt("inOtp"));
            outOtp.setText("Out OTP: "+response.getInt("outOtp"));
            boookingStatus.setText("Status: "+response.getString("status"));

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            long inTimeStamp = response.getLong("inTime");
            long outTimeStamp = response.getLong("outTime");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(inTimeStamp);
            inTime.setText("In timeStamp: "+formatter.format(calendar.getTime()));
            calendar.setTimeInMillis(outTimeStamp);
            outTime.setText("Out TimeStamp: "+formatter.format(calendar.getTime()));
            payBillButton.setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureAPIHit() {

    }
}
