package com.example.user.legaldesire.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.user.legaldesire.R;
import com.example.user.legaldesire.models.AppointmentDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.List;

import static android.support.v7.widget.ListPopupWindow.WRAP_CONTENT;

public class LawyerAppointmentAdapter extends RecyclerView.Adapter<LawyerAppointmentAdapter.MyViewHolder> {
    LayoutInflater inflater;
    private List<AppointmentDataModel>listItem;
    private Context context;
    private static final int REQUEST_PHONE_CALL = 1;
    FirebaseAuth mAuth;
    int mYear,mMonth,mDay,mHour, mMinute;
    String txtDate ,txtTime;
    String mail;





    //
    public LawyerAppointmentAdapter(  List<AppointmentDataModel> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
        Log.e("item_list_size", String.valueOf(listItem.size()));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_lawyer_appointment,parent,false);
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
       if(current.getStatus().equals("-1")) {
           holder.status.setText(Date);

        }else{
           holder.status.setText(current.getStatus());
       }


       if(current.getStatus().equals("-1")){
           holder.acceptButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                   Calendar calendar=Calendar.getInstance();
                   mYear = calendar.get(Calendar.YEAR);
                   mMonth = calendar.get(Calendar.MONTH);
                   mDay = calendar.get(Calendar.DAY_OF_MONTH);
                   txtDate="date";
                   DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                           new DatePickerDialog.OnDateSetListener() {

                               @Override
                               public void onDateSet(DatePicker view, int year,
                                                     int monthOfYear, int dayOfMonth) {

                                   txtDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                                   TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                           new TimePickerDialog.OnTimeSetListener() {

                                               @Override
                                               public void onTimeSet(TimePicker view, int hourOfDay,
                                                                     int minute) {

                                                   txtTime=(hourOfDay + ":" + minute);
                                                   setDate(txtDate,txtTime,current);

                                               }
                                           }, mHour, mMinute, false);

                                   timePickerDialog.show();



                               }
                           }, mYear, mMonth, mDay);
                   datePickerDialog.show();


//



               }
           });

       }else{

           //holder.acceptButton.setVisibility(View.GONE);
           holder.declineButton.setVisibility(View.GONE);


          // android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,160,100); 
//
         //  holder.acceptButton.setLayoutParams(lp);
           holder.acceptButton.setText("Set Reminder");
           holder.acceptButton.setWidth(WRAP_CONTENT);
           holder.acceptButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String arr[]=current.getStatus().split(" ");
                   Toast.makeText(context, "reminder", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(Intent.ACTION_EDIT);
                   intent.setType("vnd.android.cursor.item/event");
                   intent.putExtra("beginTime", arr[0]);
                   intent.putExtra("allDay", true);
                   intent.putExtra("endTime", arr[2]+24*60*60*1000);
                   intent.putExtra(CalendarContract.Events.TITLE, "Appointment of "+current.getName());
                   context.startActivity(intent);


               }
           });





       }


        holder.declineButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("ARE YOU SURE YOU WANT TO DECLINE?");
                adb.setMessage("This client will be removed from the list.");
                adb.setIcon(R.drawable.ic_delete_forever_black_24dp);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        mail=current.getMail();
                        FirebaseAuth mAuth=FirebaseAuth.getInstance();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference().child("Users").child(mail.replace(".",",")).child("appointments")
                                .child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("status").getRef().setValue("0");
                        database.getReference().child("Lawyers").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("pending_appointments")
                                .child(mail.replace(".",",")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
//                        listItem.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,listItem.size());
                            }
                        });

                    }
                });
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                adb.show();




                            }
        });



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
        holder.name.setText(current.getName().toUpperCase());

    }

    private void decline(final AppointmentDataModel current, final int position) {
        mail=current.getMail();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").child(mail.replace(".",",")).child("appointments")
                .child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("status").getRef().setValue("0");
        database.getReference().child("Lawyers").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("pending_appointments")
                .child(mail.replace(".",",")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();

            }
        });




    }

    private void setDate(final String txtDate, final String txtTime,  AppointmentDataModel current) {
          mail=current.getMail();
        Log.e("LAdatasnapshot-mail_1:", mail+" "+current.getStatus());


        Toast.makeText(context, ""+txtDate+"and time is "+txtTime, Toast.LENGTH_SHORT).show();

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        database.getReference().child("Lawyers").child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("pending_appointments")
                .child(mail.replace(".",",")).child("status").getRef().setValue(txtDate+" at "+txtTime);


        database.getReference().child("Users").child(mail.replace(".",",")).child("appointments")
                .child(mAuth.getCurrentUser().getEmail().replace(".",",")).child("status").getRef().setValue(txtDate+" at "+txtTime);


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
        Button acceptButton,declineButton;
        ImageButton mailBtn,callBtn;
        public MyViewHolder(View itemView){
            super(itemView);
            // mail=itemView.findViewById(R.id.mail);
            message=itemView.findViewById(R.id.message);
            status=itemView.findViewById(R.id.status);
            name=itemView.findViewById(R.id.name);
            callBtn = itemView.findViewById(R.id.callbtn);
            mailBtn = itemView.findViewById(R.id.emailbtn);
            acceptButton=itemView.findViewById(R.id.accept_button);
            declineButton=itemView.findViewById(R.id.decline_button);

        }

    }

}