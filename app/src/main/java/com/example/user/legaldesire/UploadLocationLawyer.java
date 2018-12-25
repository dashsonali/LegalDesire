package com.example.user.legaldesire;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class UploadLocationLawyer extends AppCompatActivity {

   private static final int LOCATION_PERMISSION_REQUEST = 1234;
    private Button uploadLocation;
    private static boolean PERMISSION_GRANTED = false;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_location_lawyer);
        uploadLocation = findViewById(R.id.uploadLocation);
        getLocationPermission();

    }

    private  void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
    }
   private void getLocationPermission(){
        String[] permissoins = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)==
                    PackageManager.PERMISSION_GRANTED){
                PERMISSION_GRANTED = true;

            }else{
                ActivityCompat.requestPermissions(UploadLocationLawyer.this,
                        permissoins
                        ,LOCATION_PERMISSION_REQUEST);
            }

        }else{
            ActivityCompat.requestPermissions(UploadLocationLawyer.this,
                    permissoins
                    ,LOCATION_PERMISSION_REQUEST);
        }
   }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PERMISSION_GRANTED = false;
        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST:{
                if(grantResults.length>0)
                {
                    for(int i =0;i<grantResults.length;i++)
                    {
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                        {
                            PERMISSION_GRANTED = false;
                            break;
                        }
                    }
                    PERMISSION_GRANTED=true;
                    initMap();
                }
            }


        }

    }
}
