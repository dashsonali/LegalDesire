package com.example.user.legaldesire.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.adapters.EmergencyContactAdapter;
import com.example.user.legaldesire.models.EmergencyContactDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EmergencyContact extends Fragment{



    FloatingActionButton addContact;
    EmergencyContactDataModel emergencyContactDataModel;
    List<EmergencyContactDataModel> listitem = new ArrayList<>();
    EmergencyContactAdapter emergencyContactAdapter;
    RecyclerView emergencyContacts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency_contact, container, false);
        addContact = view.findViewById(R.id.emergency_fab);
        emergencyContacts = view.findViewById(R.id.emergency_contacts);
        emergencyContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    EmergencyContactDialog emergencyContact_dialog = new EmergencyContactDialog();
                    emergencyContact_dialog.show(getActivity().getSupportFragmentManager(),"dialog");



            }
        });
        loadData();
        return view;
    }
    public void loadData(){
        Toast.makeText(getContext(),"Load Data",Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","))
                .child("emergency_contact");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listitem.clear();
                Toast.makeText(getContext(),"Inside Data Change",Toast.LENGTH_SHORT).show();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                { Toast.makeText(getContext(),"Inside Children",Toast.LENGTH_SHORT).show();
                    Log.e("NAME:",dataSnapshot1.child("name").getValue().toString());
                    Log.e("Contact : ",dataSnapshot1.child("contact").getValue().toString());
                    emergencyContactDataModel = new EmergencyContactDataModel(dataSnapshot1.child("name").getValue().toString(),dataSnapshot1.child("contact").getValue().toString());
                    listitem.add(emergencyContactDataModel);
                }

                emergencyContactAdapter = new EmergencyContactAdapter(listitem,getContext());
                emergencyContacts.setAdapter(emergencyContactAdapter);

             //   emergencyContactAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}
