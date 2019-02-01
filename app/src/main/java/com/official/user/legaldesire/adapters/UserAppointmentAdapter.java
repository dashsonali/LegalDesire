package com.official.user.legaldesire.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;

import android.net.Uri;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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


import com.official.user.legaldesire.R;
import com.official.user.legaldesire.models.AppointmentDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserAppointmentAdapter extends RecyclerView.Adapter<UserAppointmentAdapter.MyViewHolder> {
    LayoutInflater inflater;
    private List<AppointmentDataModel>listItem;
    private Context context;
    private static final int REQUEST_PHONE_CALL = 1;
    FirebaseAuth mAuth;



    ProgressDialog mProgressDialog;
    public UserAppointmentAdapter(  List<AppointmentDataModel> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
        Log.e("item_list_size", String.valueOf(listItem.size()));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.user_appointments,parent,false);
        mAuth=FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(context);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final    AppointmentDataModel current;
        current=listItem.get(position);
        //  holder.mail.setText(current.getMail());
        Log.e("mailinadapter",""+current.getMail()+""+current.getNumber() );
        String Date="N:N:N";
        if(current.getStatus().equals("-1")){
            holder.status.setText(Date);
            holder.cancelButton.setText("PENDING");
            holder.cancelButton.setTypeface(holder.cancelButton.getTypeface(), Typeface.BOLD_ITALIC);

            holder.cancelButton.setEnabled(false);

        }
        else   if(current.getStatus().equals("0")){
            holder.status.setText(Date);
            holder.cancelButton.setText("REQUEST DECLINED,click to remove from list");
            holder.cancelButton.setTypeface(holder.cancelButton.getTypeface(), Typeface.BOLD_ITALIC);
            holder.cancelButton.setEnabled(true);
            holder.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    database.getReference().child("Users").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("appointments").child(current.getMail().replace(".",","))
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // listItem.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,listItem.size());
                        }
                    });


                }


            });}
        if(current.getStatus().equals("0")||current.getStatus().equals("-1")){}else {
            holder.status.setText(current.getStatus());
            holder.cancelButton.setEnabled(true);
            if(holder.cancelButton.getText().toString().equals("Remove Appointment")){}else{
                holder.cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "clicked cancel", Toast.LENGTH_SHORT).show();


                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        Log.e("cancelreference", current.getMail().replace(".", ",") + "  " + mAuth.getCurrentUser().getEmail().replace(".", ",").toString());
                        database.getReference().child("Lawyers").child(current.getMail().replace(".", ",")).child("pending_appointments").child(mAuth.getCurrentUser().getEmail().replace(".", ",")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("Users").child(mAuth.getCurrentUser().getEmail().replace(".", ",")).child("appointments").child(current.getMail().replace(".", ","))
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // listItem.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, listItem.size());
                                    }
                                });


                            }
                        });


                    }
                });}


            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String curr[] = current.getStatus().split(" ");
            java.util.Date strDate = null;
            try {
                strDate = sdf.parse(curr[0]);
                Log.e("dateover12", strDate.toString() + "");

                if (new Date().after(strDate)) {
                    Log.e("dateover", strDate.toString() + "");

                    holder.cancelButton.setText("Remove Appointment");
                    holder.cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                            final AlertDialog.Builder adb = new AlertDialog.Builder(context);
                            adb.setTitle("Would you like to give Feedback?");
                            adb.setMessage("This would help us rate the Lawyer.");
                            View view1 = ((Activity) context).getLayoutInflater().inflate(R.layout.feedback, null, false);


                            adb.setView(view1);
                            final RatingBar ratingBar=view1.findViewById(R.id.ratingBar);

                            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //   Toast.makeText(context, "ready to give feedback", Toast.LENGTH_SHORT).show();
                                    mProgressDialog.setMessage("Submitting Feedback");
                                    mProgressDialog.show();
                                    // Log.e("stars",numOfStars+"");


                                    final FirebaseDatabase database=FirebaseDatabase.getInstance();
                                    database.getReference().child("Lawyers").child(current.getMail().replace(".", ",")).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            mProgressDialog.dismiss();
                                            Float rating=dataSnapshot.child("rating").getValue(Float.class);
                                            long noOfRaters= Long.parseLong(dataSnapshot.child("usersRated").getValue().toString());

                                            noOfRaters=noOfRaters+1;
                                            Float numOfStars= ratingBar.getRating();
                                            numOfStars=(numOfStars+rating)/2;
                                            database.getReference().child("Lawyers").child(current.getMail().replace(".", ",")).child("rating").setValue(numOfStars);
                                            database.getReference().child("Lawyers").child(current.getMail().replace(".", ",")).child("usersRated").setValue(noOfRaters);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    database.getReference().child("Users").child(mAuth.getCurrentUser().getEmail().replace(".", ",")).child("appointments").child(current.getMail().replace(".", ","))
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
// listItem.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, listItem.size()); }
                                    });
                                }
                            });
                            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    database.getReference().child("Users").child(mAuth.getCurrentUser().getEmail().replace(".", ",")).child("appointments").child(current.getMail().replace(".", ","))
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // listItem.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, listItem.size());}});

                                }
                            });
                            adb.show();



                        }
                    });

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        holder.mailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mailLawyer(current);
            }
        });
        holder.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLawyer(current);
            }
        });
        holder.areaofpractice.setText(current.getAreaOfPractice());
        holder.message.setText(current.getMessage());
        holder.name.setText(current.getName().toUpperCase());

    }
    private void mailLawyer(AppointmentDataModel current) {
        String email=current.getMail();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));

        context.startActivity(Intent.createChooser(emailIntent, "Choose app to mail Lawyer"));

    }

    private void callLawyer(AppointmentDataModel current) {
        String contact=current.getNumber();
        if(contact!=null)
        {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.trim()));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            }
            else
            {
                context.startActivity(intent);
            }
        }else{
            Toast.makeText(context,"Lawyer hasn't shared his/her contact",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }


    public   class MyViewHolder extends RecyclerView.ViewHolder{
        public   TextView message,status,name,areaofpractice;
        Button cancelButton;
        ImageButton mailBtn,callBtn;

        public MyViewHolder(View itemView){
            super(itemView);
            // mail=itemView.findViewById(R.id.mail);
            message=itemView.findViewById(R.id.message);
            status=itemView.findViewById(R.id.status);
            name=itemView.findViewById(R.id.name);
            callBtn = itemView.findViewById(R.id.callbtn);
            mailBtn = itemView.findViewById(R.id.emailbtn);
            cancelButton=itemView.findViewById(R.id.cancel_button);
            areaofpractice=itemView.findViewById(R.id.areaOfPracticetxt);

        }

    }

}