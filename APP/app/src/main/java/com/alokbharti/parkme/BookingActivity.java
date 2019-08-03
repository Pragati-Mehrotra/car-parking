package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.alokbharti.parkme.Interfaces.JSONArrayAPIInterface;
import com.alokbharti.parkme.Utilities.APIHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookingActivity extends AppCompatActivity implements JSONArrayAPIInterface {

    private APIHelper apiHelper;
    private String parkingName,parkingAddress;

    private TextView parkingNameTv;
    private TextView parkingAddressTv;
    private TextView slotsAvailableTv;
    private TextView slotBillMessage;
    private TextView slotBillValue;
    private TextView overTimeBillMessage;
    private TextView overTimeBillValue;
    private TextView convenienceBillMessage;
    private TextView convenienceBillValue;
    private TextView gstBillMessage;
    private TextView gstBillValue;
    private TextView totalBillMessage;
    private TextView totalBillValue;

    private RadioButton radioButton2, radioButton4, radioButton8;

    private Button payBillButton;
    private Button cancelBookingButton;

    private int slotDuration = 2, parkingId, slotsAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        setTitle("Book Parking Slots");

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        parkingId = getIntent().getIntExtra("parking_id",0);
        parkingName = getIntent().getStringExtra("parking_name");
        parkingAddress = getIntent().getStringExtra("parking_address");
        slotsAvailable = getIntent().getIntExtra("slots_available",0);

        initViews();

        apiHelper = new APIHelper(this);

        apiHelper.getBillDetails(slotDuration);
    }

    private void initViews() {
        parkingNameTv = findViewById(R.id.parking_name);
        parkingNameTv.setText(parkingName);
        parkingAddressTv = findViewById(R.id.parking_address);
        parkingAddressTv.setText(parkingAddress);
        slotsAvailableTv = findViewById(R.id.slots_available);
        slotsAvailableTv.setText(String.valueOf(slotsAvailable));

        radioButton2 = findViewById(R.id.radio_button2);
        radioButton2.setChecked(true);
        radioButton4 = findViewById(R.id.radio_button4);
        radioButton8 = findViewById(R.id.radio_button8);

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slotDuration = 2;
                apiHelper.getBillDetails(slotDuration);
            }
        });

        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slotDuration = 4;
                apiHelper.getBillDetails(slotDuration);
            }
        });

        radioButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slotDuration = 8;
                apiHelper.getBillDetails(slotDuration);
            }
        });

        slotBillMessage = findViewById(R.id.slot_bill_message);
        slotBillValue = findViewById(R.id.slotBillValue);
        overTimeBillMessage = findViewById(R.id.overtime_bill_message);
        overTimeBillValue = findViewById(R.id.overtimeBillValue);
        convenienceBillMessage = findViewById(R.id.convenience_bill_message);
        convenienceBillValue = findViewById(R.id.convenienceBillValue);
        gstBillMessage = findViewById(R.id.gst_bill_message);
        gstBillValue = findViewById(R.id.gstBillValue);
        totalBillMessage = findViewById(R.id.total_bill_message);
        totalBillValue = findViewById(R.id.totalBillValue);
    }

    @Override
    public void onSuccessfulHit(JSONArray response) {
        try {
            for(int i =0; i<response.length(); i++){
                JSONObject jsonObject = response.getJSONObject(i);
                switch (i){
                    case 0: slotBillMessage.setText(jsonObject.getString("message"));
                            slotBillValue.setText(jsonObject.getString("value"));
                            break;
                    case 1: overTimeBillMessage.setText(jsonObject.getString("message"));
                            overTimeBillValue.setText(jsonObject.getString("value"));
                            break;
                    case 2: convenienceBillMessage.setText(jsonObject.getString("message"));
                            convenienceBillValue.setText(jsonObject.getString("value"));
                            break;
                    case 3: gstBillMessage.setText(jsonObject.getString("message"));
                            gstBillValue.setText(jsonObject.getString("value"));
                            break;
                    case 4: totalBillMessage.setText(jsonObject.getString("message"));
                            totalBillValue.setText(jsonObject.getString("value"));
                            break;
                    default: break;        
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailureAPIHit() {
        Toast.makeText(this, "Failed to get bill details!!", Toast.LENGTH_SHORT).show();
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
