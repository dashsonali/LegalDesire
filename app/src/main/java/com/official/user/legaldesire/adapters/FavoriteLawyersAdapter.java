package com.official.user.legaldesire.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.official.user.legaldesire.R;
import com.official.user.legaldesire.models.LawyerData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FavoriteLawyersAdapter extends RecyclerView.Adapter<FavoriteLawyersAdapter.MyViewHolder> {

    List<LawyerData> listitem = new ArrayList<>();
    Context context;
    private static final int REQUEST_PHONE_CALL = 1;
    ProgressBar mProgressBar;
    ImageView propic;
    public FavoriteLawyersAdapter(List<LawyerData> listItem, Context mContext)
    {
        this.listitem = listItem;
        this.context = mContext;
    }
    @Override
    public FavoriteLawyersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_lawyer_cell,parent,false);
        return new FavoriteLawyersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final    LawyerData current;
        current=listitem.get(position);
        loadProfilePic(current);
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
                LocateOnMap(current);

            }
        });
        holder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Lawyer Removed From Favorites",Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","))
                        .child("favorite_lawyers").child(current.getEmail().replace(".",",")).removeValue();
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLawyer(current);

            }
        });
        holder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailLawyer(current);
            }
        });
    }
    private void mailLawyer(LawyerData current) {
        String email=current.getEmail();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));

        context.startActivity(Intent.createChooser(emailIntent, "Choose app to mail Lawyer"));

    }
    public void loadProfilePic(LawyerData current){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages/").child("Lawyers/").child(current.getEmail());
        if(storageReference!=null)
        {
            Log.e("Picstorange","Not Null");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(context).load(uri.toString()).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(propic);
                    Log.e("imageuriString",uri.toString());
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressBar.setVisibility(View.GONE);
                    //Toast.makeText(context,"Please Upload A Profile Pic",Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private void LocateOnMap(LawyerData current) {
        if(current.getLocate().equals("noLocation")){
            Toast.makeText(context, "The Lawyer has not shared  location", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(current.getLocate()));
            context.startActivity(intent);}
    }

    private void callLawyer(LawyerData current) {
        String contact=current.getContact();
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
        return listitem.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name,areaOfPractice,noOfRaters;
        RatingBar ratingBar;
        Button locate,bookApointment;
        ImageButton call,mail,filtertn,favoriteBtn;
        public MyViewHolder(View itemView) {
            super(itemView);
            locate=itemView.findViewById(R.id.locatebtn);
            name=itemView.findViewById(R.id.nametxt);
            propic = itemView.findViewById(R.id.profilePic);
            areaOfPractice=itemView.findViewById(R.id.areaOfPracticetxt);
            call=itemView.findViewById(R.id.callbtn);
            mail=itemView.findViewById(R.id.emailbtn);
//            email=itemView.findViewById(R.id.emailtxt);
//            contact=itemView.findViewById(R.id.contacttxt);
            favoriteBtn = itemView.findViewById(R.id.bookbtn);
            ratingBar=itemView.findViewById(R.id.ratingtxt);
            noOfRaters=itemView.findViewById(R.id.noOfReviewstxt);
            bookApointment=itemView.findViewById(R.id.bookAPPbtn);
            mProgressBar = itemView.findViewById(R.id.profilePicProgresspar);
        }
    }
}

