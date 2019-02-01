package com.official.user.legaldesire.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.official.user.legaldesire.fragments.BookAppointment;
import com.official.user.legaldesire.models.LawyerData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
      LayoutInflater inflater;
     private List<LawyerData>listItem;
    private Context context;
    private static final int REQUEST_PHONE_CALL = 1;
 ProgressBar mProgressBar;
    ImageView propic;

//
    public RecyclerAdapter(  List<LawyerData> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
        Log.e("item_list_size", String.valueOf(listItem.size()));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_cell,parent,false);
     return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
     final    LawyerData current;
        current=listItem.get(position);
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
        holder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Lawyer Added To Favorites",Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","))
                        .child("favorite_lawyers").child(current.getEmail().replace(".",",")).setValue(current);
            }
        });
        holder.bookApointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FragmentTransaction ft = FragmentTransaction.beginTransaction();

                BookAppointment bookAppointment =new BookAppointment();
                bookAppointment.lawyerData=current;
               // bookAppointment.show()//
                bookAppointment.show(((AppCompatActivity) context).getSupportFragmentManager(),"");
            }
        });

       // //holder.lawyerData=current;

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

    private void mailLawyer(LawyerData current) {
        String email=current.getEmail();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));

        context.startActivity(Intent.createChooser(emailIntent, "Choose app to mail Lawyer"));

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
        return listItem.size();
    }


  public   class MyViewHolder extends RecyclerView.ViewHolder{
      public   TextView name,areaOfPractice,noOfRaters;
      RatingBar ratingBar;
      Button locate,bookApointment;
      ImageButton call,mail,filtertn,favoriteBtn;
      //String location,number,email,contact;
     // LawyerData lawyerData;
        public MyViewHolder(View itemView){
            super(itemView);
            locate=itemView.findViewById(R.id.locatebtn);
            name=itemView.findViewById(R.id.nametxt);
            propic = itemView.findViewById(R.id.profilePic);
            propic.setScaleType(ImageView.ScaleType.CENTER);
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