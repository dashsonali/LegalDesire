package com.official.user.legaldesire.fragments;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.official.user.legaldesire.R;
import com.official.user.legaldesire.adapters.UserAppointmentAdapter;
import com.official.user.legaldesire.models.AppointmentDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserAppointmentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<AppointmentDataModel>appointmentDataModels;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
   // ProgressDialog progressDialog;
    ProgressBar mProgressBar;
    Context mContext;

    private String mParam1;
    private String mParam2;

    public static UserAppointmentFragment newInstance(String param1, String param2) {
        UserAppointmentFragment fragment = new UserAppointmentFragment();
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

        View rootView= inflater.inflate(R.layout.fragment_user_appointment, container, false);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
       mProgressBar = rootView.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        appointmentDataModels=new ArrayList<>();
        loadRecyclerViewData();

        return rootView;

    }



    private void loadRecyclerViewData() {
        mAuth = FirebaseAuth.getInstance();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = database.getReference().child("Users").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("appointments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("datasnapshot", dataSnapshot.toString());
                mProgressBar.setVisibility(View.GONE);
                appointmentDataModels.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.e("datasnapshot1",dataSnapshot1.toString() );
                    String email,name,number,areaofpractice;
                    String message;
                    String status;
                    message=dataSnapshot1.child("message").getValue(String.class);
                    status=dataSnapshot1.child("status").getValue(String.class);
                    email=dataSnapshot1.child("mail").getValue(String.class);
                    name=dataSnapshot1.child("name").getValue(String.class);
                    number=dataSnapshot1.child("number").getValue(String.class);

                    areaofpractice=dataSnapshot1.child("areaOfPractice").getValue(String.class)+" Lawyer";
                    AppointmentDataModel current=new AppointmentDataModel(
                            message,
                            email,
                            status,
                            name,
                            number,
                            areaofpractice
                    );
                    appointmentDataModels.add(current);


                    adapter =new UserAppointmentAdapter(appointmentDataModels,getContext());
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


