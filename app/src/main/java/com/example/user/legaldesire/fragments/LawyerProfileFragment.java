package com.example.user.legaldesire.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.user.legaldesire.LoginActivity;
import com.example.user.legaldesire.R;
import com.example.user.legaldesire.UploadLocationLawyer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class LawyerProfileFragment extends Fragment{


    private  TextView name,email,areaOfPractice,phone,address,feeTxt,usersRated;
    SharedPreferences sharedPreferences;
    private RatingBar ratingBar;
    private ImageView user_menu,propic;
    ProgressBar mProgressBar;
    public LawyerProfileFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lawyer_profile, container, false);
        name = view.findViewById(R.id.nametxt);
        usersRated  = view.findViewById(R.id.usersRatedtext);
        areaOfPractice = view.findViewById(R.id.areaOfPracticeTxt);
        ratingBar = view.findViewById(R.id.ratingtxt);
        phone = view.findViewById(R.id.phoneTxt);
        email = view.findViewById(R.id.emailTxt);
        feeTxt = view.findViewById(R.id.feeTxt);
        user_menu = view.findViewById(R.id.user_menu);
        address = view.findViewById(R.id.addressTxt);
        propic = view.findViewById(R.id.profilePic);
        mProgressBar = view.findViewById(R.id.profilePicProgresspar);
        sharedPreferences = getContext().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name",null));
        ratingBar.setRating(Float.valueOf(sharedPreferences.getString("rating",null)));
        usersRated.setText(sharedPreferences.getString("usersRated",null)+" "+"client(s) have rated");
        areaOfPractice.setText(sharedPreferences.getString("areaOfPractice",null)+" "+"Lawyer");
        phone.setText(sharedPreferences.getString("contact",null));
        email.setText(sharedPreferences.getString("email",null));
       // Log.e("consultationFee",sharedPreferences.getString("consultationFee",null));
        feeTxt.setText(sharedPreferences.getString("consultationFee","Not Assigned!"));
        address.setText(sharedPreferences.getString("address","Please Update Your Address In Settings so that clients can find you."));
        loadProfilePic();
        user_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_pop_up(view);
            }
        });
        return view;
    }
    public void loadProfilePic(){

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ProfileImages/").child("Lawyers/").child(FirebaseAuth.getInstance().getCurrentUser().getEmail());



                Log.e("Picstorange","Not Null");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Glide.with(getActivity()).load(uri.toString()).listener(new RequestListener<String, GlideDrawable>() {
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
                        int errorCode = ((StorageException) e).getErrorCode();
                        if(errorCode == StorageException.ERROR_OBJECT_NOT_FOUND){
                            mProgressBar.setVisibility(View.GONE);
                           // Toast.makeText(getContext(),"Please Upload A Profile Pic",Toast.LENGTH_SHORT).show();
                        }

                    }
                });








    }
    public void show_pop_up(View v){
        PopupMenu popupMenu = new PopupMenu(getActivity().getBaseContext(),v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.logout)
                {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().commit();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    startActivity(new Intent(getActivity().getBaseContext(),LoginActivity.class));
                    getActivity().finish();
                    return true;
                }else if(id == R.id.edit_profile){
                    return true;
                }else if(id == R.id.bookmarks){
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container,new BookmarkFragment()).commit();

                }else if(id==R.id.uploadLocation)
                {
                    startActivity(new Intent(getContext(), UploadLocationLawyer.class));
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.lawyer_menu);
        popupMenu.show();

    }




}
