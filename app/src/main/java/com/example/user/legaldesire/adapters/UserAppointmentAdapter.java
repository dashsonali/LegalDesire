package com.example.user.legaldesire.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.SearchLawer;
import com.example.user.legaldesire.fragments.BookAppointment;
import com.example.user.legaldesire.fragments.LawyerRecycler;
import com.example.user.legaldesire.models.AppointmentDataModel;
import com.example.user.legaldesire.models.LawyerData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.graphics.Typeface.ITALIC;

public class UserAppointmentAdapter extends RecyclerView.Adapter<UserAppointmentAdapter.MyViewHolder> {
    LayoutInflater inflater;
    private List<AppointmentDataModel>listItem;
    private Context context;
    private static final int REQUEST_PHONE_CALL = 1;
    FirebaseAuth mAuth;



    //
    public UserAppointmentAdapter(  List<AppointmentDataModel> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
        Log.e("item_list_size", String.valueOf(listItem.size()));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.user_appointments,parent,false);
        mAuth=FirebaseAuth.getInstance();

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
        else {holder.status.setText(current.getStatus());
              holder.cancelButton.setEnabled(true);
              holder.cancelButton.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      Toast.makeText(context, "clicked cancel", Toast.LENGTH_SHORT).show();


                      final FirebaseDatabase database = FirebaseDatabase.getInstance();
                      Log.e("cancelreference", current.getMail().replace(".",",")+"  "+mAuth.getCurrentUser().getEmail().replace(".",",").toString());
                      database.getReference().child("Lawyers").child(current.getMail().replace(".",",")).child("pending_appointments").child(mAuth.getCurrentUser().getEmail().replace(".",",")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              database.getReference().child("Users").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("appointments").child(current.getMail().replace(".",","))
                                      .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      listItem.remove(position);
                                      notifyItemRemoved(position);
                                      notifyItemRangeChanged(position,listItem.size());
                                  }
                              });


                          }
                      });



                  }
              });
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

        holder.message.setText(current.getMessage());
        holder.name.setText(current.getName());

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
        public   TextView message,status,name;
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

        }

    }

}