package com.official.user.legaldesire.fragments;

import android.Manifest;
import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.official.user.legaldesire.MainActivity;

import com.official.user.legaldesire.R;
import com.official.user.legaldesire.SingleShotLocationProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class UserMenuFragment extends Fragment {


    Context mContext;
  //  private Button b,findLawyer;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener listener;
    ProgressDialog progressDialog;

    CardView send_sos,find_lawyers,emergencyContactBtn,call_police,call_fire,call_ambulance;
    Location mlocation;
    Geocoder geocoder;
    String city;
    SingleShotLocationProvider.GPSCoordinates cordinates=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.user_menu_fragment,null);
        send_sos = view.findViewById(R.id.send_sos);

       // b = (Button) view.findViewById(R.id.SOS);
        find_lawyers=view.findViewById(R.id.find_lawyers);
        emergencyContactBtn = view.findViewById(R.id.emergency_contactsBtn);
        progressDialog=new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);
        call_ambulance =view.findViewById(R.id.call_ambulance);
        call_police = view.findViewById(R.id.call_police);
        call_fire = view.findViewById(R.id.call_fire);
        call_ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PRIVILEGED)!=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.CALL_PRIVILEGED},11);
                }else{
                    Uri callUri = Uri.parse("tel://102");
                    Intent callIntent = new Intent(Intent.ACTION_CALL,callUri);
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    startActivity(callIntent);
                }

            }
        });
        call_fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PRIVILEGED)!=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.CALL_PRIVILEGED},11);
                }else{
                    Uri callUri = Uri.parse("tel://101");
                    Intent callIntent = new Intent(Intent.ACTION_CALL,callUri);
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    startActivity(callIntent);
                }

            }
        });
        call_police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(mContext,Manifest.permission.CALL_PRIVILEGED)!=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.CALL_PRIVILEGED},11);
                }else{
                    Uri callUri = Uri.parse("tel://100");
                    Intent callIntent = new Intent(Intent.ACTION_CALL,callUri);
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                    startActivity(callIntent);
                }

            }
        });
        locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
        find_lawyers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("action","search_lawyer");
                if(cordinates!=null){
                    intent.putExtra("location",city.toString());
                    //Log.e("locationinfindLawyer",""+mlocation);

                }else{
                    getLocation(mContext);
                }
                startActivity(intent);




            }
        });
        emergencyContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("action","emergency_contact");

                startActivity(intent);
            }
        });
        configure_button();



        send_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                progressDialog.setMessage("Sending SOS...");
                    progressDialog.show();
                if( ActivityCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    progressDialog.dismiss();
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.SEND_SMS}
                            , 10);
                }else{
                    if(cordinates!=null)
                    {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","));
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                progressDialog.dismiss();
                                if(dataSnapshot.hasChild("emergency_contact"))
                                {
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.child("emergency_contact").getChildren())
                                    {
                                        Toast.makeText(mContext, "SOS MESSAGE IS SENT TO ALL EMERGENCY CONTACT", Toast.LENGTH_SHORT).show();
                                        String contact = dataSnapshot1.child("contact").getValue().toString();
                                        SmsManager smsManager = SmsManager.getDefault();
                                        StringBuffer smsBody = new StringBuffer();
                                        smsBody.append(mContext.getSharedPreferences("MyPref",Context.MODE_PRIVATE).getString("name","****")+" needs your help "+"\n Locate on maps :\n"+"http://maps.google.com/maps?q=");
                                        smsBody.append(cordinates.latitude);
                                        smsBody.append(",");
                                        smsBody.append(cordinates.longitude);
                                        smsManager.sendTextMessage(contact, null, smsBody.toString(), null, null);
                                        Log.e("sms", smsBody.toString());

                                    }


                                }else{
                                    Toast.makeText(mContext,"You do not have any emergency contact!",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        getLocation(mContext);
                        Toast.makeText(mContext,"Wait for location update!",Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

        return view;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            case 11:
                Toast.makeText(mContext,"Permissions granted! Try again",Toast.LENGTH_SHORT);
                default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.SEND_SMS}
                        , 10);
            }

        } else {
            Toast.makeText(mContext,"Getting loction",Toast.LENGTH_SHORT).show();
            getLocation(mContext);

        }



    }
    public void getLocation(Context context) {
       progressDialog.show();
        progressDialog.setMessage("Getting Location");
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                       cordinates = location;

                        progressDialog.dismiss();
                        geocoder=new Geocoder(mContext, Locale.getDefault());
                double lat=cordinates.latitude;
               double longti=  location.longitude;
                try {
                    List<Address> addresses  = geocoder.getFromLocation(lat,longti, 1);
                    city = addresses.get(0).getAdminArea();
                    Log.e("CITIYNAME",city.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }



                    }
                });

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
