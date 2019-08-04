package com.alokbharti.parkme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alokbharti.parkme.Adapter.ParkingAdapter;
import com.alokbharti.parkme.Interfaces.LocationInterface;
import com.alokbharti.parkme.Interfaces.RecyclerViewClickListener;
import com.alokbharti.parkme.Pojo.ParkingInfo;
import com.alokbharti.parkme.Utilities.APIHelper;

import java.util.List;

public class ParkingActivity extends AppCompatActivity implements LocationInterface, RecyclerViewClickListener {

    private APIHelper apiHelper;
    private RecyclerView parkingRecyclerView;
    private ParkingAdapter parkingAdapter;
    private List<ParkingInfo> parkingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
        setTitle("Nearby Parkings");

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        apiHelper = new APIHelper(this);
        parkingRecyclerView = findViewById(R.id.parkingRecyclerView2);
        parkingRecyclerView.setHasFixedSize(true);
        parkingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        double latitude = getIntent().getDoubleExtra("latitude",0);
        double longitude = getIntent().getDoubleExtra("longitude",0);
        apiHelper.getParkingNearby(latitude,longitude);
    }

    @Override
    public void onGetParkingList(List<ParkingInfo> parkingList) {
        this.parkingList = parkingList;
        if(parkingList.size()==0){
            Toast.makeText(this, "No parking slots available in your area", Toast.LENGTH_SHORT).show();
        }else {
            parkingAdapter = new ParkingAdapter(parkingList, this);
            parkingRecyclerView.setAdapter(parkingAdapter);
        }
    }

    @Override
    public void onFailedGettingparkingList() {
        Toast.makeText(this, "Failed to get parking list", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        ParkingInfo parkingInfo = parkingList.get(position);
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("parking_id", parkingInfo.getParkingId());
        intent.putExtra("parking_name", parkingInfo.getParkingName());
        intent.putExtra("parking_address", parkingInfo.getParkingAddress());
        intent.putExtra("slots_available", parkingInfo.getAvailableSlots());
        startActivity(intent);
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
