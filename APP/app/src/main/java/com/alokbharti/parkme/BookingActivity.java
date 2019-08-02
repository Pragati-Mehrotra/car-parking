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
import android.widget.RadioButton;
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
    private LinearLayout bookingDetailsLinearLayout;
    private EditText couponCode;
    private Button couponCodeSubmitButton;
    private RadioButton googlePayRB;

    private TextView bookingId;
    private TextView parkingAddressTv;
    private TextView bill;
    private TextView slotDuration;
    private TextView inTime;
    private Button payBillButton;
    private Button cancelBookingButton;
    private int newBookingId;
    private int parkingId;
    String slotDurationValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setTitle("Book Parking Slots");
        initViews();

        apiHelper = new APIHelper(this);

        parkingId = getIntent().getIntExtra("parking_id", 0);
        parkingAddress = getIntent().getStringExtra("parking_address");
    }

    private void initViews() {
        couponCode = findViewById(R.id.coupon_code_et);
        couponCodeSubmitButton = findViewById(R.id.coupon_code_button);
        googlePayRB = findViewById(R.id.google_pay_radiobutton);
        bookingLinearLayout = findViewById(R.id.booking_ll);
        bookingDetailsLinearLayout = findViewById(R.id.booking_detail_ll);
        bookingDetailsLinearLayout.setVisibility(View.GONE);
        bookingSlotDuration = findViewById(R.id.booking_slot_duration);
        bookingButton = findViewById(R.id.booking_button);
        bookingId = findViewById(R.id.booking_id);
        parkingAddressTv = findViewById(R.id.parking_address);
        bill = findViewById(R.id.bill);
        slotDuration = findViewById(R.id.slot_duration);
        inTime = findViewById(R.id.in_time);
        payBillButton = findViewById(R.id.pay_bill_button);
        payBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(googlePayRB.isChecked()){
                    Intent intent = new Intent(BookingActivity.this, ActiveBooking.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(BookingActivity.this, "Please select at least one payment method", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBookingButton = findViewById(R.id.cancel_booking);
        cancelBookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingLinearLayout.setVisibility(View.VISIBLE);
                bookingDetailsLinearLayout.setVisibility(View.GONE);
            }
        });

        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slotDurationValue = bookingSlotDuration.getText().toString();
                if(TextUtils.isEmpty(slotDurationValue)){
                    bookingSlotDuration.setError("This is required");
                    return;
                }
                bookingLinearLayout.setVisibility(View.GONE);
                bookingDetailsLinearLayout.setVisibility(View.VISIBLE);
                long timeStamp = System.currentTimeMillis();
                apiHelper.getBookingDetails(currentUserId, parkingId, Integer.parseInt(slotDurationValue), timeStamp);
            }
        });

        couponCodeSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coupon = couponCode.getText().toString();
                if(TextUtils.isEmpty(coupon)){
                    couponCode.setError("fill correct coupon code");
                    return;
                }

                if(coupon.equals("ParkMe20")){
                    double billToBePaid = Double.parseDouble(bill.getText().toString());
                    billToBePaid = billToBePaid - billToBePaid*(0.2);
                    bill.setText(String.format("Amount to be paid: %s", String.valueOf(billToBePaid)));
                }
            }
        });
    }

    @Override
    public void onSuccessfulHit(JSONObject response) {
        Log.e("response: ",response.toString());
        parkingAddressTv.setText(String.format("Parking Address: %s", parkingAddress));
        try {
            newBookingId = response.getInt("bookingId");
            bookingId.setText(String.format(Locale.ENGLISH,"Booking Id: %d", newBookingId));
            bill.setText(String.format("Amount to be paid: %s", response.getDouble("bill")));
            slotDuration.setText(String.format(Locale.ENGLISH,"SlotDuration: %d", response.getInt("slotDuration")));

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
}
