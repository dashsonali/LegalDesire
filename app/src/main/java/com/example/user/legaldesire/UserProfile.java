 package com.example.user.legaldesire;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.legaldesire.R;

public class UserProfile extends AppCompatActivity {

    private Button b;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;
    ProgressDialog progressDialog;
Location mlocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        //t = (TextView) findViewById(R.id.textView);
        b = (Button) findViewById(R.id.SOS);
        progressDialog=new ProgressDialog(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               mlocation=location;
                progressDialog.dismiss();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();
        progressDialog.setMessage("fetching location");
        progressDialog.show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET,Manifest.permission.SEND_SMS}
                        ,10);
            }

        }else{                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission


                //Log.e("location",mlocation.toString() );
                SmsManager smsManager = SmsManager.getDefault();
                StringBuffer smsBody = new StringBuffer();
                smsBody.append(" http://maps.google.com/maps?q=" );
                smsBody.append(mlocation.getLatitude());
                smsBody.append(",");
                smsBody.append(mlocation.getLongitude());
                smsManager.sendTextMessage("9398888408", null, smsBody.toString(), null, null);
                Log.e("sms",smsBody.toString() );


            }
        });
    }
}

