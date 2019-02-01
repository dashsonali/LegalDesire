package com.official.user.legaldesire.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.official.user.legaldesire.R;
import com.official.user.legaldesire.adapters.EmergencyContactAdapter;
import com.official.user.legaldesire.models.EmergencyContactDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EmergencyContact extends Fragment{

    Context mContext;

    ProgressBar mPrgoressBar;
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
        mPrgoressBar = view.findViewById(R.id.progressBar);

        emergencyContacts.setLayoutManager(new LinearLayoutManager(mContext));
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
        Toast.makeText(mContext,"Load Data",Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","))
                .child("emergency_contact");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listitem.clear();
                mPrgoressBar.setVisibility(View.GONE);
                Toast.makeText(mContext,"Inside Data Change",Toast.LENGTH_SHORT).show();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                { Toast.makeText(mContext,"Inside Children",Toast.LENGTH_SHORT).show();
                    Log.e("NAME:",dataSnapshot1.child("name").getValue().toString());
                    Log.e("Contact : ",dataSnapshot1.child("contact").getValue().toString());
                    emergencyContactDataModel = new EmergencyContactDataModel(dataSnapshot1.child("name").getValue().toString(),dataSnapshot1.child("contact").getValue().toString(),dataSnapshot1.getKey());
                    listitem.add(emergencyContactDataModel);
                }

                emergencyContactAdapter = new EmergencyContactAdapter(listitem,mContext);
                emergencyContacts.setAdapter(emergencyContactAdapter);

             //   emergencyContactAdapter.notifyDataSetChanged();

            }

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
