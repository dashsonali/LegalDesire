package com.example.user.legaldesire.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.fragments.EmergencyContact;
import com.example.user.legaldesire.models.EmergencyContactDataModel;

import java.util.List;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.MyViewHolder> {

    Context mContext;
    List<EmergencyContactDataModel> listitem;
    EmergencyContactDataModel emergencyContactDataModel;
    public EmergencyContactAdapter(List<EmergencyContactDataModel> listitem, Context mContext){
        Toast.makeText(mContext,String.valueOf(listitem.size()),Toast.LENGTH_SHORT).show();
        this.listitem = listitem;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cell,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        emergencyContactDataModel = listitem.get(position);
        holder.name.setText(emergencyContactDataModel.getName());
        holder.contact.setText(emergencyContactDataModel.getContact());
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder {


        TextView name,contact;
        public MyViewHolder(View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
        }
    }
}
