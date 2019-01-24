package com.example.user.legaldesire.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.adapters.FavoriteLawyersAdapter;
import com.example.user.legaldesire.adapters.RecyclerAdapter;
import com.example.user.legaldesire.models.LawyerData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FavoriteLawyers extends Fragment {
    private List<LawyerData> listItems;
    List<LawyerData> newListItems;
    RecyclerView recyclerView;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    FavoriteLawyersAdapter adapter;
    ProgressDialog progressDialog;
    ImageButton filterbtn;
    Context mContext;
    AutoCompleteTextView locationEditText;
    ImageButton filterBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_lawyer_recycler, container, false);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        progressDialog = new ProgressDialog(mContext);
        locationEditText = rootView.findViewById(R.id.editText);
        filterBtn = rootView.findViewById(R.id.filterBtn);
        locationEditText.setVisibility(View.GONE);
        filterBtn.setVisibility(View.GONE);
        listItems=new ArrayList<>();
        loadRecyclerViewData();
        return rootView;

    }

    private void loadRecyclerViewData() {
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = database.getReference().child("Users").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("favorite_lawyers");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
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
                    String lat="noLat",longi="noLongi",location="noLocation";
                    name=dataSnapshot1.child("name").getValue(String.class);
                    contact=dataSnapshot1.child("contact").getValue(String.class);
                    email=dataSnapshot1.child("email").getValue(String.class);
                    areaOfPractice=dataSnapshot1.child("areaOfPractice").getValue(String.class);
                    rating=dataSnapshot1.child("rating").getValue(Float.class);
                    noOfRaters=dataSnapshot1.child("noOfRaters").getValue().toString();
                    lat=dataSnapshot1.child("location").child("latitude").getValue(String.class);
                    longi=dataSnapshot1.child("location").child("longitude").getValue(String.class);
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
                    listItems.add(current);
                    progressDialog.dismiss();
                    adapter = new FavoriteLawyersAdapter(listItems,mContext);
                    recyclerView.setAdapter(adapter);


                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }
}
