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
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mSupportMapFragment;
    private FusedLocationProviderClient mFusedLocation;
    private View mMapView;
    private Location mLocation;

    double currentLat = 0;
    double currentLong = 0;
    private final float DEFAULT_ZOOM = 18;

    private String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Assign variables
        mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mSupportMapFragment.getMapAsync(this);

        //Initialise map view
        mMapView = mSupportMapFragment.getView();

        //Initialise Fused Location Client
        mFusedLocation = LocationServices.getFusedLocationProviderClient(MapActivity.this);

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
        LatLng UNSW = new LatLng(-33.91586831036975, 151.2310599212734);
        mMap.addMarker(new MarkerOptions().position(UNSW).title("UNSW"));

        LatLng Melbourne = new LatLng(-37.1907646138579, 146.6425273618467);
        mMap.addMarker(new MarkerOptions().position(Melbourne).title("Melbourne"));

        LatLng Nyngan = new LatLng(-31.59353172062354, 147.16842135428314);
        mMap.addMarker(new MarkerOptions().position(Nyngan).title("Nyngan"));

        LatLng Cairns = new LatLng(-16.77758146400933, 145.67668061682897);
        mMap.addMarker(new MarkerOptions().position(Cairns).title("Cairns"));



        //Set position of camera to current position
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UNSW));

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