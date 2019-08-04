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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.alokbharti.parkme.Utilities.GlobalConstants.currentUserId;
import static com.alokbharti.parkme.Utilities.SavedSharedPreferences.setUserId;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationInterface, OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSION_REQUEST_LOCATION = 1000;

    private APIHelper apiHelper;
//    private RecyclerView parkingRecyclerView;
//    private ParkingAdapter parkingAdapter;
    private double latitude, longitude;
    Button searchAddressButton;
    FloatingActionButton locationButton;
    EditText searchAddressEditText;
    TextView latLong;
    private GoogleMap mMap;
    private Marker marker;
    private List<Marker> parkingMarkers;
    private List<ParkingInfo> allParkingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Set Location");

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
        parkingMarkers = new ArrayList<>();

//        parkingRecyclerView = findViewById(R.id.parkingRecyclerView);
//        parkingRecyclerView.setHasFixedSize(true);
//        parkingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationButton = findViewById(R.id.locationButton);
        searchAddressButton = findViewById(R.id.search_address_button);
        searchAddressEditText = findViewById(R.id.location_search_edit_text);
        searchAddressEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("key pressed",String.valueOf(keyCode));
                String address = searchAddressEditText.getText().toString();
                if(TextUtils.isEmpty(address)){
                    searchAddressEditText.setError("This is required");
                }else{
                    getParkingListFromAddress(address);
                }
                return false;
            }
        });
        searchAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this ,ParkingActivity.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                startActivity(intent);
            }
        });
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
                }else{
                    Log.e("permission granted","yes");
                    getparkinglistFromGPS();
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
                searchAddressEditText.setText(location.getAddressLine(0));
                System.out.println(location.toString());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                marker.setPosition(new LatLng(latitude, longitude));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
//                latLong.setText("Latitude: " + latitude + ", Longitude: " + longitude);
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
                            searchAddressEditText.setText("My location");
                            // Logic to handle location object
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            marker.setPosition(new LatLng(latitude, longitude));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
//                            latLong.setText(latitude+", "+longitude);
                            apiHelper.getParkingNearby(latitude, longitude);
                        }else{
                            Log.e("failed to get lat", ":(");
                            Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSION_REQUEST_LOCATION){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // permission was granted
                getparkinglistFromGPS();
                Toast.makeText(this, "Location Found", Toast.LENGTH_SHORT).show();
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
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (id == R.id.nav_active_booking) {
            startActivity(new Intent(this, ActiveBooking.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onGetParkingList(List<ParkingInfo> parkingList) {
        Log.e("parking succesful", String.valueOf(parkingList.size()));
        allParkingList = parkingList;
        if(parkingList.size()==0){
            Toast.makeText(this, "No parking slots available in your area", Toast.LENGTH_SHORT).show();
        }else {
            if(parkingMarkers.size() > 0)
                for(Marker marker: parkingMarkers)
                    marker.remove();
            parkingMarkers.clear();
            for(ParkingInfo parking: parkingList){
                LatLng latLng = new LatLng(parking.getLatitude(), parking.getLongitude());
                Marker m = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(parking.getParkingName()));
                parkingMarkers.add(m);
            }
        }
    }

    @Override
    public void onFailedGettingparkingList() {
        Toast.makeText(this, "Failed to get parking list", Toast.LENGTH_SHORT).show();
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                System.out.println(latLng.toString());
                marker.setPosition(latLng);
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                latLong.setText("Latitude: " + latLng.latitude + ", Longitude: " + latLng.longitude);

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String address = addresses.get(0).getAddressLine(0);
                    searchAddressEditText.setText(address);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                apiHelper.getParkingNearby(latLng.latitude, latLng.longitude);
            }
        });
    }
}
