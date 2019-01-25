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
import com.example.user.legaldesire.R;
import com.example.user.legaldesire.adapters.LawyerAppointmentAdapter;

import com.example.user.legaldesire.models.AppointmentDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LawyerAppointmentFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<AppointmentDataModel>appointmentDataModels;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context=getContext();

        View rootView= inflater.inflate(R.layout.fragment_user_appointment, container, false);
        recyclerView=rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        progressDialog=new ProgressDialog(context);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentDataModels=new ArrayList<>();

        loadRecyclerViewData();
        return rootView;

    }



    private void loadRecyclerViewData() {
        mAuth = FirebaseAuth.getInstance();
        progressDialog.setMessage("Fetching data..");
        progressDialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = database.getReference().child("Lawyers").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("pending_appointments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                Log.e("data_snapshot", dataSnapshot.toString());
                appointmentDataModels.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.e("data_snapshot_1",dataSnapshot1.toString());
                    String email,name,number;
                    String message;
                    String status;
                    message=dataSnapshot1.child("message").getValue(String.class);
                    status=dataSnapshot1.child("status").getValue(String.class);
                    email=dataSnapshot1.child("email").getValue(String.class);
                    name=dataSnapshot1.child("name").getValue(String.class);
                    number=dataSnapshot1.child("number").getValue(String.class);
                    if(status!=null){
                    if(status.equals("-1")) {
                        AppointmentDataModel current = new AppointmentDataModel(
                                message,
                                email,
                                status,
                                name,
                                number
                        );
                        appointmentDataModels.add(current);
                    }}



                    adapter =new LawyerAppointmentAdapter(appointmentDataModels,getContext());
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}


