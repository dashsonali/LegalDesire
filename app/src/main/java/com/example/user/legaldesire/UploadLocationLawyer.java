package com.example.user.legaldesire;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.user.legaldesire.adapters.PlaceAutocompleteAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private FloatingActionButton myLocationBtn;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    GoogleApiClient mGoogleApiCleint;
    GeoDataClient mGeoDataClient;
    LocationRequest mLocationRequest;
    Marker marker;
    private RelativeLayout rl;
    ProgressDialog mProgressDialog;
    FusedLocationProviderClient mFusedLocationClient;
    Marker current_location_marker;
    ImageButton filterButton;
    AutoCompleteTextView search;
    private static final LatLngBounds LAT_LNG_BOUNDS=new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_location_lawyer);
        uploadLocation = findViewById(R.id.uploadLocation);
         uploadLocation.setOnClickListener(this);
        rl = findViewById(R.id.mapLayout);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mProgressDialog = new ProgressDialog(this);
        search=findViewById(R.id.search_places);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        myLocationBtn = findViewById(R.id.current_location);
        myLocationBtn.setOnClickListener(this);
        filterButton = findViewById(R.id.filterBtn);
        filterButton.setOnClickListener(this);
        initMap();

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiCleint=new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        placeAutocompleteAdapter =new PlaceAutocompleteAdapter(this,mGeoDataClient,LAT_LNG_BOUNDS,null);
        search.setAdapter(placeAutocompleteAdapter);


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
                mMap.setMyLocationEnabled(false);
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Please Turn On Your GPS", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("On Location Changed", "CALLED!");
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (current_location_marker != null) {
                current_location_marker.remove();

            }
            Circle mCircle = mMap.addCircle(new CircleOptions()
            .center(latLng)
            .radius(500)
            .strokeWidth(0)
            .strokeColor(Color.parseColor("#2271cce7"))
            .fillColor(Color.parseColor("#2271cce7")));
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("This your current postion!")
                    .icon(BitmapDescriptorFactory.fromBitmap( resizeIcon("my_location",100,100)));

            current_location_marker = mMap.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            mMap.animateCamera(cameraUpdate);
        }
    }

    public Bitmap resizeIcon(String icon,int width,int height)
    {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(icon,"drawable",getPackageName()));
        Bitmap resizedImage = Bitmap.createScaledBitmap(imageBitmap,width,height,false);

        return resizedImage;
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // mLocationRequest.setInterval(1000);
        Log.e("On Connected", "CALLED!");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.e("Permission", "Granted!");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiCleint, mLocationRequest, this);

        } else {
            Log.e("Permission", "Requested!");
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

        if (marker != null) {
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("This position will be uploaded!")
                .icon(BitmapDescriptorFactory.fromBitmap(resizeIcon("upload_location",100,100)));
        marker = mMap.addMarker(markerOptions);
        LatLng latLng1 = marker.getPosition();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.uploadLocation) {
            if(marker.getPosition()!=null){
                mProgressDialog.setMessage("Uploading your location...");
                mProgressDialog.show();
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = null;
                try {
                    addresses = gc.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String locality = addresses.get(0).getLocality();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String mail = getIntent().getExtras().getString("lawyer_mail");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Lawyers").child(mail);
                databaseReference.child("location").child("latitude").setValue(String.valueOf(marker.getPosition().latitude));
                databaseReference.child("location").child("longitude").setValue(String.valueOf(marker.getPosition().longitude));

                if (locality != null)
                    databaseReference.child("location").child("locality").setValue(locality);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("type", "lawyer");

                editor.commit();
                Snackbar snackbar = Snackbar.make(rl, "Your Location Is Uploaded",
                        Snackbar.LENGTH_INDEFINITE);

                snackbar.show();
                // Toast.makeText(getApplicationContext(),"Location Uploaded",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadLocationLawyer.this, LoginActivity.class));
                mProgressDialog.dismiss();
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Please Tap On A Location!",Toast.LENGTH_SHORT);
            }


        } else if (id == R.id.current_location) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiCleint, mLocationRequest, this);

            }
        }else if(id==R.id.filterBtn)
        {
            PopupMenu popupMenu = new PopupMenu(UploadLocationLawyer.this,filterButton);
            popupMenu.getMenuInflater().inflate(R.menu.map_types,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    switch (id)
                    {
                        case R.id.sattelite_view:
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            return true;
                        case R.id.default_view:
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            return true;
                        case R.id.terrain_view:
                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            return true;
                        case R.id.hybrid_view:
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            return true;

                    }
                    return true;
                }
            });
        }

    }
}
