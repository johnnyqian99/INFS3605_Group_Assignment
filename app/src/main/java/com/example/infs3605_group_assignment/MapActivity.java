package com.example.infs3605_group_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private SupportMapFragment mSupportMapFragment;
    //private FusedLocationProviderClient mFusedClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Assign variables
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mSupportMapFragment.getMapAsync(this);

        //Initialise fused location
        //mFusedClient = LocationServices.getFusedLocationProviderClient(this);

    }


    //Add marker for UNSW
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng UNSW = new LatLng(-33.91586831036975, 151.2310599212734);
        map.addMarker(new MarkerOptions().position(UNSW).title("UNSW"));

        LatLng Melbourne = new LatLng(-37.1907646138579, 146.6425273618467);
        map.addMarker(new MarkerOptions().position(Melbourne).title("Melbourne"));

        LatLng Nyngan = new LatLng(-31.59353172062354, 147.16842135428314);
        map.addMarker(new MarkerOptions().position(Nyngan).title("Nyngan"));

        LatLng Cairns = new LatLng(-16.77758146400933, 145.67668061682897);
        map.addMarker(new MarkerOptions().position(Cairns).title("Cairns"));

        map.moveCamera(CameraUpdateFactory.newLatLng(UNSW));

    }
}