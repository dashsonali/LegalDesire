package com.example.user.legaldesire.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.user.legaldesire.R;
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


public class LawyerRecycler extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<LawyerData>listItems;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ImageButton filterbtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static LawyerRecycler newInstance(String param1, String param2) {
        LawyerRecycler fragment = new LawyerRecycler();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context=getContext();

        View rootView= inflater.inflate(R.layout.fragment_lawyer_recycler, container, false);
        progressDialog=new ProgressDialog(context);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        filterbtn=rootView.findViewById(R.id.filterBtn);
        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(getContext(),filterbtn);
                        popup.getMenuInflater().inflate(R.menu.filter,popup.getMenu());

                        popup.show();


            }
        });



        // adapter=new RecyclerAdapter(getActivity(),getData());
       // recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems=new ArrayList<>();
        loadRecyclerViewData();
       /* for(int i=0;i<10;i++){
            LawyerData current=new LawyerData(
                    "name"+i
            );
            listItems.add(current);

        }
        adapter =new RecyclerAdapter(listItems,getContext());*/

        return rootView;

    }

    private void loadRecyclerViewData() {
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = database.getReference().child("Lawyers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("datasnapshot", dataSnapshot.toString());
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.e("datasnapshot1",dataSnapshot1.toString() );
                            String name;
                    String contact;
                    String email;
                    String areaOfPractice;
                    Float noOfRaters ;
                    Float rating;
                    String lat="noLat",longi="noLongi",location="noLocation";
                    name=dataSnapshot1.child("name").getValue(String.class);
                    contact=dataSnapshot1.child("contact").getValue(String.class);
                    email=dataSnapshot1.child("email").getValue(String.class);
                    areaOfPractice=dataSnapshot1.child("areaOfPractice").getValue(String.class);
                    rating=dataSnapshot1.child("rating").getValue(Float.class);
                    noOfRaters=dataSnapshot1.child("usersRated").getValue(Float.class);
                    lat=dataSnapshot1.child("location").child("latitude").getValue(String.class);
                    longi=dataSnapshot1.child("location").child("longitude").getValue(String.class);
                        if(lat==null){location="noLocation";}else{
                        location=    "http://maps.google.com/maps?q="+lat+","+longi;}



                    Log.e("locationMap",location+"" );
                            LawyerData current=new LawyerData(
                                    name,
                                    email,
                                    areaOfPractice,
                                    contact,
                                    rating,
                                    noOfRaters+" customers reviews",
                                    location
                            );
                            listItems.add(current);
                            progressDialog.dismiss();
                    adapter =new RecyclerAdapter(listItems,getContext());
                    recyclerView.setAdapter(adapter);


            }}

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });







    }}


