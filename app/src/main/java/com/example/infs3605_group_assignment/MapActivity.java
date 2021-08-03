package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mSupportMapFragment;
    private BottomNavigationView bottomNavigationView;
    private FusedLocationProviderClient mFusedLocation;
    private View mMapView;
    private Location mLocation;

    double currentLat = 0;
    double currentLong = 0;
    private final float DEFAULT_ZOOM = 50;

    private String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Assign variables
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mSupportMapFragment.getMapAsync(this);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Initialise map view
        mMapView = mSupportMapFragment.getView();

        //Initialise Fused Location Client
        mFusedLocation = LocationServices.getFusedLocationProviderClient(MapActivity.this);

        // ***NAVIGATION BAR

        // Set current selected item
        bottomNavigationView.setSelectedItemId(R.id.map);
        // Set up select listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.map:
                        return true;
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext(), NewsDemoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.post:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.donate:
                        startActivity(new Intent(getApplicationContext(), RewardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        // NAVIGATION BAR***
    }


    //Preparing the map
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Handled by PermissionsActivity
        mMap.setMyLocationEnabled(true);
        //Shows my Location Button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //Call method to set markers
        setCoordinates();
        //Set position of location button to bottom right
        setLocationButton();


    }

    public void setCoordinates() {
        //Hardcoded coordinates
        LatLng post1 = new LatLng(-32.7772752497915, 151.3047420299347);
        mMap.addMarker(new MarkerOptions().position(post1).title("Alternate Food Sources"));

        LatLng post2 = new LatLng(-16.744147873708283, 145.65719275331506);
        mMap.addMarker(new MarkerOptions().position(post2).title("Great Barrier Reef Disappointment"));

        LatLng post3 = new LatLng(-32.17969034341798, 148.61351363793872);
        mMap.addMarker(new MarkerOptions().position(post3).title("Mask Littering"));

        LatLng post4 = new LatLng(-35.29914043014169, 149.0364927702417);
        mMap.addMarker(new MarkerOptions().position(post4).title("Disgusting rubbish dump"));

        LatLng post5 = new LatLng(-13.092218806991267, 132.3938708560954);
        mMap.addMarker(new MarkerOptions().position(post5).title("National Tree Day"));

        LatLng post6 = new LatLng(-33.81376368578107, 150.85556558042967);
        mMap.addMarker(new MarkerOptions().position(post6).title("Go Woolies!"));

        LatLng post7 = new LatLng(-32.01912417394958, 115.89587138720785);
        mMap.addMarker(new MarkerOptions().position(post7).title("Flash Flooding Alert"));

        LatLng post8 = new LatLng(-32.8793404565499, 151.78878241607444);
        mMap.addMarker(new MarkerOptions().position(post8).title("Green Tree Frogs"));

        LatLng post9 = new LatLng(-34.988800148866055, 143.68309842893942);
        mMap.addMarker(new MarkerOptions().position(post9).title("Dry Murray Darling Basin"));

        LatLng post10 = new LatLng(-33.35178955912811, 149.59980294043305);
        mMap.addMarker(new MarkerOptions().position(post10).title("Kangaroo out and about!"));

        LatLng post11 = new LatLng(-23.445425970342914, 133.69643648638498);
        mMap.addMarker(new MarkerOptions().position(post11).title("Climate change thoughts"));

        LatLng post12 = new LatLng(-24.35052106830736, 150.62094215975762);
        mMap.addMarker(new MarkerOptions().position(post12).title("Blown up coal plant"));

        LatLng post13 = new LatLng(-33.57972782014727, 150.43845402983257);
        mMap.addMarker(new MarkerOptions().position(post13).title("Gardening Tips"));

        LatLng post14 = new LatLng(-33.91357239688895, 151.15345807542909);
        mMap.addMarker(new MarkerOptions().position(post14).title("Food scrap bin"));

        LatLng post15 = new LatLng(-34.069525898177, 150.62770452239565);
        mMap.addMarker(new MarkerOptions().position(post15).title("Protect our farmers"));

        LatLng post16 = new LatLng(-31.839538438310825, 152.4811387141347);
        mMap.addMarker(new MarkerOptions().position(post16).title("Beautiful Koalas"));




        //Set position of camera to current position
        mMap.moveCamera(CameraUpdateFactory.newLatLng(post1));

    }

    public void setLocationButton() {
        if (mMapView != null && mMapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }
    }

    public void getLocation() {
        //this is null???
        //currentLat = mLocation.getLatitude();
        //currentLong = mLocation.getLongitude();

       // Log.d(TAG, "Current Location Coordinates: " + currentLat + ", " + currentLong);

    }


}