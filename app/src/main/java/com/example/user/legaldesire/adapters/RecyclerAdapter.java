package com.example.user.legaldesire.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.models.LawyerData;

import java.util.List;
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
      LayoutInflater inflater;
     private List<LawyerData>listItem;
    private Context context;
    LawyerData current;
    private static final int REQUEST_PHONE_CALL = 1;



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
         current=listItem.get(position);
        holder.name.setText(current.getName());
        holder.areaOfPractice.setText(current.getAreaOfPractice()+" Lawyer");
        Log.e("current.getemail", current.getEmail());
       holder.ratingBar.setNumStars(5);
       holder.ratingBar.setMax(5);
        holder.ratingBar.setStepSize(0.5f);
        holder.ratingBar.setRating(Float.parseFloat(String.valueOf(current.getRating())));
        holder.noOfRaters.setText(current.getNoOfRaters());
        holder.locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLawyer(current.getContact());

            }
        });
        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailLawyer(current.getEmail());
            }
        });
        //holder.lawyerData=current;

    }

    private void mailLawyer(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));

        context.startActivity(Intent.createChooser(emailIntent, "Choose app to mail Lawyer"));

    }

    private void callLawyer(String contact) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.trim()));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        else
        {
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }


  public   class MyViewHolder extends RecyclerView.ViewHolder{
      public   TextView name,areaOfPractice,noOfRaters;
      RatingBar ratingBar;
      Button locate;
      ImageButton call,mail;
      //String location,number,email,contact;
     // LawyerData lawyerData;
        public MyViewHolder(View itemView){
            super(itemView);
            locate=itemView.findViewById(R.id.locatebtn);
            name=itemView.findViewById(R.id.nametxt);
            areaOfPractice=itemView.findViewById(R.id.areaOfPracticetxt);
            call=itemView.findViewById(R.id.callbtn);
            mail=itemView.findViewById(R.id.emailbtn);
//            email=itemView.findViewById(R.id.emailtxt);
//            contact=itemView.findViewById(R.id.contacttxt);
            ratingBar=itemView.findViewById(R.id.ratingtxt);
            noOfRaters=itemView.findViewById(R.id.noOfReviewstxt);

  }

}}
