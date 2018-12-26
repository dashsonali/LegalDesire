package com.example.user.legaldesire;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class UploadLocationLawyer extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener
        , View.OnClickListener {
    private String[] permissoins = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int LOCATION_PERMISSION_REQUEST = 1234;
    private Button uploadLocation;
    private static boolean PERMISSION_GRANTED = false;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiCleint;
    LocationRequest mLocationRequest;
    Marker marker;
    private RelativeLayout rl;
    ProgressDialog mProgressDialog;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_location_lawyer);
        uploadLocation = findViewById(R.id.uploadLocation);
        //   uploadLocation.setOnClickListener(this);
        rl = findViewById(R.id.mapLayout);
        mProgressDialog = new ProgressDialog(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initMap();

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public synchronized void createGoogleApiClient() {
        mGoogleApiCleint = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiCleint.connect();
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                PERMISSION_GRANTED = true;
                mMap.setMyLocationEnabled(true);
                createGoogleApiClient();

            } else {
                ActivityCompat.requestPermissions(UploadLocationLawyer.this,
                        permissoins
                        , LOCATION_PERMISSION_REQUEST);
            }

        } else {
            ActivityCompat.requestPermissions(UploadLocationLawyer.this,
                    permissoins
                    , LOCATION_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PERMISSION_GRANTED = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            PERMISSION_GRANTED = false;
                            break;
                        }
                    }
                    PERMISSION_GRANTED = true;
                    getLocationPermission();
                }
            }


        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLocationPermission();
        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Please Turn On Your GPS", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("On Location Changed","CALLED!");
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(cameraUpdate);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // mLocationRequest.setInterval(1000);
        Log.e("On Connected","CALLED!");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission","Granted!");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiCleint, mLocationRequest, this);
        }else{
            Log.e("Permission","Requested!");
            ActivityCompat.requestPermissions(UploadLocationLawyer.this,
                    permissoins
                    , LOCATION_PERMISSION_REQUEST);
        }




    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

            if(marker!=null)
            {
                marker.remove();
            }
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("This position will be uploaded!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        marker = mMap.addMarker(markerOptions);
        LatLng latLng1 = marker.getPosition();
    }

    @Override
    public void onClick(View view) {
        mProgressDialog.setMessage("Uploading your location...");
        mProgressDialog.show();
            int id = view.getId();
            if(id == R.id.uploadLocation){
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = null;
                try {
                     addresses = gc.getFromLocation(marker.getPosition().latitude,marker.getPosition().longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String locality = addresses.get(0).getLocality();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String mail = getIntent().getExtras().getString("lawyer_mail");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Lawyers").child(mail);
                databaseReference.child("location").child("latitude").setValue(String.valueOf(marker.getPosition().latitude));
                databaseReference.child("location").child("longitude").setValue(String.valueOf(marker.getPosition().longitude));

                if(locality!=null)
                databaseReference.child("location").child("locality").setValue(locality);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("type","lawyer");

                editor.commit();
                Snackbar snackbar = Snackbar.make(rl, "Your Location Is Uploaded",
                        Snackbar.LENGTH_INDEFINITE);

                snackbar.show();
               // Toast.makeText(getApplicationContext(),"Location Uploaded",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadLocationLawyer.this,LoginActivity.class));
                mProgressDialog.dismiss();
                finish();

            }
    }
}
