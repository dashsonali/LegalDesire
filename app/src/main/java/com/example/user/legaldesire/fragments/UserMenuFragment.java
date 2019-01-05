package com.example.user.legaldesire.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.adapters.ViewPagerAdapter;

import static android.content.Context.LOCATION_SERVICE;

public class UserMenuFragment extends Fragment {


  //  private Button b,findLawyer;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;
    ProgressDialog progressDialog;

    CardView send_sos,find_lawyers;
    Location mlocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.user_menu_fragment,null);
        send_sos = view.findViewById(R.id.send_sos);

       // b = (Button) view.findViewById(R.id.SOS);
        find_lawyers=view.findViewById(R.id.find_lawyers);
        find_lawyers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new LawyerRecycler()).commit();
                Fragment selectFragment=new LawyerRecycler();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();


            }
        });
        progressDialog=new ProgressDialog(getContext());
        locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
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
        if(mlocation==null)
        {
            configure_button();
            progressDialog.setMessage("Fetching data..");
            progressDialog.show();
        }
        return view;
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

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.SEND_SMS}
                        , 10);
            }

        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        send_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission


                //Log.e("location",mlocation.toString() );
                SmsManager smsManager = SmsManager.getDefault();
                StringBuffer smsBody = new StringBuffer();
                smsBody.append(" http://maps.google.com/maps?q=");
                smsBody.append(mlocation.getLatitude());
                smsBody.append(",");
                smsBody.append(mlocation.getLongitude());
                smsManager.sendTextMessage("9658463402", null, smsBody.toString(), null, null);
                Log.e("sms", smsBody.toString());
                Toast.makeText(getActivity().getBaseContext(), "SOS MESSAGE IS SENT TO EMERGENCY CONTACTS", Toast.LENGTH_SHORT).show();


            }
        });

    }



}
