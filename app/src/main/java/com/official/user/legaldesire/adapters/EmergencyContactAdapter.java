package com.official.user.legaldesire.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.official.user.legaldesire.R;
import com.official.user.legaldesire.fragments.EmergencyContactDialog;
import com.official.user.legaldesire.models.EmergencyContactDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.MyViewHolder> {

    Context mContext;
    List<EmergencyContactDataModel> listitem;

    DatabaseReference databaseReference;
    public EmergencyContactAdapter(List<EmergencyContactDataModel> listitem, Context mContext){
        if(listitem.size()==0)
        {
            Toast.makeText(mContext,"No Emergency Contact Added",Toast.LENGTH_SHORT).show();
        }
        this.listitem = listitem;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cell,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final EmergencyContactDataModel emergencyContactDataModel = listitem.get(position);
        holder.name.setText(emergencyContactDataModel.getName());
        holder.contact.setText(emergencyContactDataModel.getContact());
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(emergencyContactDataModel);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editContact(emergencyContactDataModel);


            }
        });
    }

    public void editContact(EmergencyContactDataModel emergencyContactDataModel)
    {
        EmergencyContactDialog emergencyContactDialog = new EmergencyContactDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name",emergencyContactDataModel.getName());
        bundle.putString("key",emergencyContactDataModel.getKey());
        bundle.putString("contact",emergencyContactDataModel.getContact());
        emergencyContactDialog.setArguments(bundle);
       android.support.v4.app.FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
        emergencyContactDialog.show(fragmentManager,"contact_dialog");
    }
    public void delete(EmergencyContactDataModel emergencyContactDataModel)
    {
      FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance()
        .getCurrentUser().getEmail().replace(".",",")).child("emergency_contact").child(emergencyContactDataModel.getKey()).removeValue();

    }
    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder {

        ImageView cross,edit;
        TextView name,contact;
        public MyViewHolder(View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
            cross = itemView.findViewById(R.id.cross);
            edit = itemView.findViewById(R.id.edit);
        }
    }
}
