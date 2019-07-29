package com.alokbharti.parkme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.alokbharti.parkme.Adapter.ParkingAdapter;
import com.alokbharti.parkme.Interfaces.LocationInterface;
import com.alokbharti.parkme.Interfaces.RecyclerViewClickListener;
import com.alokbharti.parkme.Pojo.ParkingInfo;
import com.alokbharti.parkme.Utilities.APIHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;
import static com.alokbharti.parkme.Utilities.SavedSharedPreferences.setUserId;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationInterface, RecyclerViewClickListener, OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSION_REQUEST_LOCATION = 1000;

    private APIHelper apiHelper;
    private RecyclerView parkingRecyclerView;
    private ParkingAdapter parkingAdapter;
    private double latitude, longitude;
    Button searchAddressButton;
    EditText searchAddressEditText;
    TextView latLong;
    private GoogleMap mMap;
    private Marker marker;
    private List<ParkingInfo> allParkingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        apiHelper = new APIHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
        }else{
            Log.e("permission granted","yes");
            getparkinglistFromGPS();
        }

        latLong = findViewById(R.id.debugging_lat_long);

        parkingRecyclerView = findViewById(R.id.parkingRecyclerView);
        parkingRecyclerView.setHasFixedSize(true);
        parkingRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchAddressButton = findViewById(R.id.search_address_button);
        searchAddressEditText = findViewById(R.id.location_search_edit_text);
        searchAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = searchAddressEditText.getText().toString();
                if(TextUtils.isEmpty(address)){
                    searchAddressEditText.setError("This is required");
                    return;
                }else{
                    getParkingListFromAddress(address);
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void getParkingListFromAddress(String userAddress){
        Geocoder coder = new Geocoder(this);
        try {
            List<Address> address = coder.getFromLocationName(userAddress, 5);
            if (address==null) {
                Toast.makeText(this, "Failed to get parking slots at this location", Toast.LENGTH_LONG).show();
            }else {
                Address location = address.get(0);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                marker.setPosition(new LatLng(latitude, longitude));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                latLong.setText("Latitude: " + latitude + ", Longitude: " + longitude);
                apiHelper.getParkingNearby(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void getparkinglistFromGPS(){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            marker.setPosition(new LatLng(latitude, longitude));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                            latLong.setText("Latitude: "+latitude+", Longitude: "+longitude);
                            apiHelper.getParkingNearby(latitude, longitude);
                        }else{
                            Log.e("failed to get lat", ":(");
                        }
                    }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSION_REQUEST_LOCATION){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // permission was granted
                getparkinglistFromGPS();
            }else{
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            setUserId(MainActivity.this, currentUserId, false);
            startActivity(new Intent(this, LandingPageActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            startActivity(new Intent(this,ProfileActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onGetParkingList(List<ParkingInfo> parkingList) {
        Log.e("parking succesful", ":)");
        allParkingList = parkingList;
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
        ParkingInfo parkingInfo = allParkingList.get(position);
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("parking_id", parkingInfo.getParkingId());
        intent.putExtra("parking_address", parkingInfo.getParkingAddress());
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(latitude, longitude);
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Bike current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setMinZoomPreference(13.0f);
    }
}
