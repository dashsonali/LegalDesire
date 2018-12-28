package com.example.user.legaldesire.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.models.LawyerData;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
      LayoutInflater inflater;
     private List<LawyerData>listItem;
    private Context context;

    public RecyclerAdapter(List<LawyerData> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_cell,parent,false);
     return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LawyerData current=listItem.get(position);
        holder.name.setText(current.getName());
        holder.areaOfPractice.setText(current.getAreaOfPractice());
        holder.email.setText(current.getEmail());
        holder.contact.setText(current.getContact());
       holder.ratingBar.setNumStars(5);
       holder.ratingBar.setMax(5);
        holder.ratingBar.setStepSize(0.5f);
        holder.ratingBar.setRating(Float.parseFloat(String.valueOf(current.getRating())));
        holder.noOfRaters.setText(current.getNoOfRaters());

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }


  public   class MyViewHolder extends RecyclerView.ViewHolder{
      public   TextView name,areaOfPractice,email,contact,noOfRaters;
      RatingBar ratingBar;
        public MyViewHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.nametxt);
            areaOfPractice=itemView.findViewById(R.id.areaOfPracticetxt);
            email=itemView.findViewById(R.id.emailtxt);
            contact=itemView.findViewById(R.id.contacttxt);
            ratingBar=itemView.findViewById(R.id.ratingtxt);
            noOfRaters=itemView.findViewById(R.id.noOfReviewstxt);


        }
    }

}
