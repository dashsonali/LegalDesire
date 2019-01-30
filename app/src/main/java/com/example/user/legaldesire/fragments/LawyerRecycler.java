package com.example.user.legaldesire.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.adapters.PlaceAutocompleteAdapter;
import com.example.user.legaldesire.adapters.RecyclerAdapter;
import com.example.user.legaldesire.models.LawyerData;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LawyerRecycler extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<LawyerData>listItems;
    List<LawyerData> newListItems;
    String city;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ImageButton filterbtn;
    AutoCompleteTextView searchButton;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    GeoDataClient mGeoDataClient;
    private static final LatLngBounds LAT_LNG_BOUNDS=new LatLngBounds(
            new LatLng(-40,-168),new LatLng(71,136)
    );
    Geocoder geocoder;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context=getContext();

        View rootView= inflater.inflate(R.layout.fragment_lawyer_recycler, container, false);
        progressDialog=new ProgressDialog(context);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        searchButton =rootView.findViewById(R.id.editText);
        recyclerView.setHasFixedSize(true);
        mGeoDataClient = Places.getGeoDataClient(getContext(), null);

        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_1)
                .setCountry("INDIA")
                .build();
      //  placeAutocompleteAdapter =new PlaceAutocompleteAdapter(getContext(),mGeoDataClient,LAT_LNG_BOUNDS,autocompleteFilter);
        placeAutocompleteAdapter =new PlaceAutocompleteAdapter(getContext(),mGeoDataClient,LAT_LNG_BOUNDS,null);

        searchButton.setAdapter(placeAutocompleteAdapter);
        Bundle arguments = getArguments();

        city = arguments.getString("location");
        Log.e("locationinfindLawyer",""+city);
        filterbtn=rootView.findViewById(R.id.filterBtn);
        searchButton.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Log.e("onItemSelected",""+city);


                if(placeAutocompleteAdapter.getContext().getApplicationContext()==null){}else{

                   if(placeAutocompleteAdapter.getItem(2)!=null){
                       // String addr2=  placeAutocompleteAdapter.getItem(1).toString();
                       String addr2=  searchButton.getText().toString();


                        String addr21[]=addr2.split(",");

                          if(addr21.length>1) {
                              city = addr21[addr21.length - 2];
                              Log.e("onItemSelected12", "" + addr21[addr21.length - 2] + "");
                              loadRecyclerViewData(city);
                          }else {
                              Toast.makeText(getContext(), "Enter atleast a state", Toast.LENGTH_SHORT).show();

                          }


                    }else {
                        Toast.makeText(getContext(), "Enter a state", Toast.LENGTH_SHORT).show();
                    }




                }







    }
        });



             /*   if(searchButton.getText()!=null){
                    String searchText=searchButton.getText().toString();
                    Log.e("cityinsearch",""+searchText);

                    String search[]=searchText.split(",");
                    if(search.length>1){
                        city=search[search.length-1];
                        Log.e("cityinsearch",""+search[search.length-2]);

                    }}*/





           // pl.setOnPlaceSelectedListener




        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getContext(), filterbtn);
                popup.getMenuInflater().inflate(R.menu.filter, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String attr;

                        switch (item.getItemId()) {

                            case R.id.criminal:
                                Toast.makeText(getContext(), "criminal", Toast.LENGTH_SHORT).show();
                                attr="Criminal";
                                manageData(attr,listItems);
                                return true;


                            case R.id.civil:
                                Toast.makeText(getContext(), "civil", Toast.LENGTH_SHORT).show();
                                 attr="Civil";
                                manageData(attr,listItems);
                                return true;

                            case R.id.family:
                                Toast.makeText(getContext(), "family", Toast.LENGTH_SHORT).show();
                                attr="Family";
                                manageData(attr,listItems);
                                return true;
                            case R.id.insurance:
                                Toast.makeText(getContext(), "insurance", Toast.LENGTH_SHORT).show();
                                attr="Insurance";
                                manageData(attr,listItems);
                                return true;
                            case R.id.arbitration:
                                Toast.makeText(getContext(), "arbitration", Toast.LENGTH_SHORT).show();
                                attr="Arbitration";
                                manageData(attr,listItems);
                                return true;

                            case R.id.corporation:
                                Toast.makeText(getContext(), "corporate", Toast.LENGTH_SHORT).show();
                                attr="Corporate";
                                manageData(attr,listItems);
                                return true;

                        }
                        return false;
                    }

                });

                popup.show();


            }});






        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems=new ArrayList<>();
        loadRecyclerViewData();



        return rootView;

    }

    private void manageData(String attr, List<LawyerData> listItems) {

        newListItems=new ArrayList<>();
        Log.e("size", String.valueOf(listItems.size()));
        for (int i=0;i<listItems.size();i++){

            if(listItems.get(i).getAreaOfPractice().equals(attr)){
                Log.e("attribute", listItems.get(i).getAreaOfPractice());
                newListItems.add(listItems.get(i));

            }else {
                Log.e("attributeNot", listItems.get(i).getAreaOfPractice());

                // newListItems.remove(listItems.get(i));
            }}
           RecyclerAdapter adapter1=new RecyclerAdapter(newListItems,getContext());
            recyclerView.setAdapter(adapter1);

        }



    private void loadRecyclerViewData() {
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = database.getReference().child("Lawyers");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("datasnapshot", dataSnapshot.toString());
listItems.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.e("datasnapshot1",dataSnapshot1.toString() );
                            String name;
                    String contact;
                    String email;
                    String areaOfPractice;
                    String noOfRaters ;
                    Float rating;
                    String state;
                    String lat="noLat",longi="noLongi",location="noLocation";
                    name=dataSnapshot1.child("name").getValue(String.class);
                    contact=dataSnapshot1.child("contact").getValue(String.class);
                    email=dataSnapshot1.child("email").getValue(String.class);
                    areaOfPractice=dataSnapshot1.child("areaOfPractice").getValue(String.class);
                    rating=dataSnapshot1.child("rating").getValue(Float.class);
                    noOfRaters=dataSnapshot1.child("usersRated").getValue().toString();
                    lat=dataSnapshot1.child("location").child("latitude").getValue(String.class);
                    longi=dataSnapshot1.child("location").child("longitude").getValue(String.class);
                    state=dataSnapshot1.child("location").child("state").getValue(String.class);

                    if(lat==null){location="noLocation";}else{
                        location= "http://maps.google.com/maps?q="+lat+","+longi;}



                    Log.e("locationMap",location+"" );
                            LawyerData current=new LawyerData(
                                    name,
                                    email,
                                    areaOfPractice,
                                    contact,
                                    rating,
                                    noOfRaters+" client reviews",
                                    location
                            );
                    Log.e("locationinfindLawyer",""+state);
                    if(state!=null||city!=null){

                    if(city.toUpperCase().equals(
                            state.toUpperCase())){
                            listItems.add(current);}}
                            progressDialog.dismiss();
                    adapter = new RecyclerAdapter(listItems,getContext());
                    recyclerView.setAdapter(adapter);


            }}

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });







    }

    private void loadRecyclerViewData(final String city1) {
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = database.getReference().child("Lawyers");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("datasnapshot", dataSnapshot.toString());
                listItems.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.e("datasnapshot1",dataSnapshot1.toString() );
                    String name;
                    String contact;
                    String email;
                    String areaOfPractice;
                    String noOfRaters ;
                    Float rating;
                    String state;
                    String lat="noLat",longi="noLongi",location="noLocation";
                    name=dataSnapshot1.child("name").getValue(String.class);
                    contact=dataSnapshot1.child("contact").getValue(String.class);
                    email=dataSnapshot1.child("email").getValue(String.class);
                    areaOfPractice=dataSnapshot1.child("areaOfPractice").getValue(String.class);
                    rating=dataSnapshot1.child("rating").getValue(Float.class);
                    noOfRaters=dataSnapshot1.child("usersRated").getValue().toString();
                    lat=dataSnapshot1.child("location").child("latitude").getValue(String.class);
                    longi=dataSnapshot1.child("location").child("longitude").getValue(String.class);
                    state=dataSnapshot1.child("location").child("state").getValue(String.class);

                    if(lat==null){location="noLocation";}else{
                        location= "http://maps.google.com/maps?q="+lat+","+longi;}



                    Log.e("locationMap",location+"" );
                    LawyerData current=new LawyerData(
                            name,
                            email,
                            areaOfPractice,
                            contact,
                            rating,
                            noOfRaters+" client reviews",
                            location
                    );
                    Log.e("locationinfindLawyer",""+state);
                    Log.e("currentcity1",""+current.toString()+""+city1+",");

                    if(state!=null){

                        if(city1.trim().toUpperCase().equals(state.toUpperCase())){
                            listItems.add(current);
                            Log.e("currentcity1",""+current.toString()+"");

                        }}
                    progressDialog.dismiss();
                    adapter = new RecyclerAdapter(listItems,getContext());
                    recyclerView.setAdapter(adapter);


                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }
}


