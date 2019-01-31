package com.example.user.legaldesire.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
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

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.Locale;


public class LawyerAppointmentAdapter extends RecyclerView.Adapter<LawyerAppointmentAdapter.MyViewHolder> {
    LayoutInflater inflater;
    private List<AppointmentDataModel> listItem;
    private Context context;
    private static final int REQUEST_PHONE_CALL = 1;
    FirebaseAuth mAuth;
    int mYear, mMonth, mDay, mHour, mMinute;
    String txtDate, txtTime;
    String mail;
    String choosenDate;


    //
    public LawyerAppointmentAdapter(List<AppointmentDataModel> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
        Log.e("item_list_size", String.valueOf(listItem.size()));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_lawyer_appointment, parent, false);
        mAuth = FirebaseAuth.getInstance();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AppointmentDataModel current;
        current = listItem.get(position);
        //  holder.mail.setText(current.getMail());
        Log.e("mailinadapter", "" + current.getMail() + "" + current.getNumber());
        final String Date = "N:N:N";
        if (current.getStatus().equals("-1")) {
            holder.status.setText(Date);

        } else {
            holder.status.setText(current.getStatus());
        }


        if (current.getStatus().equals("-1")) {
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final Calendar calendar = Calendar.getInstance();
                    mYear = calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH);
                    mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    txtDate = "date";
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                                    //java.util.Date date = new Date(year,monthOfYear,dayOfMonth-1);
                                    // String dayOfTheWeek = simpleDateFormat.format(date);
                                    txtDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                    Date date = calendar.getTime();
                                    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
                                    choosenDate = dateFormat.format(date);
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                            new TimePickerDialog.OnTimeSetListener() {

                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                                      int minute) {
                                                    int hour = hourOfDay;
                                                    int minutes = minute;
                                                    String timeSet = "";
                                                    if (hour > 12) {
                                                        hour -= 12;
                                                        timeSet = "PM";
                                                    } else if (hour == 0) {
                                                        hour += 12;
                                                        timeSet = "AM";
                                                    } else if (hour == 12) {
                                                        timeSet = "PM";
                                                    } else {
                                                        timeSet = "AM";
                                                    }

                                                    String min = "";
                                                    if (minutes < 10)
                                                        min = "0" + minutes;
                                                    else
                                                        min = String.valueOf(minutes);

                                                    // Append in a StringBuilder
                                                    String aTime = new StringBuilder().append(hour).append(':')
                                                            .append(min).append(" ").append(timeSet).toString();

                                                    setDate(txtDate, aTime, current);

                                                }
                                            }, mHour, mMinute, false);

                                    timePickerDialog.show();


                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();


//


                }
            });

        } else {

            //holder.acceptButton.setVisibility(View.GONE);
            holder.declineButton.setVisibility(View.GONE);


            // android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,160,100);
//
            //  holder.acceptButton.setLayoutParams(lp);
            holder.acceptButton.setText("Set Reminder");
            //   holder.acceptButton.setWidth(MATCH_PARENT);
            holder.acceptButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 10);
                    } else {
                        String arr[] = current.getStatus().split(" ");
                        Toast.makeText(context, "reminder" + arr[0], Toast.LENGTH_SHORT).show();
                        String arr1[] = arr[0].split("-");
                        int day = Integer.valueOf(arr1[0]);
                        int month = Integer.valueOf(arr1[1]);
                        int year = Integer.valueOf(arr1[2]);

                        String arr2[] = arr[2].split(":");

                        String time = arr[2].trim()+" "+arr[3].trim();
                        if(arr[3].equals("AM"))
                        {

                        }else{

                        }
                        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm",Locale.US);
                        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a",Locale.US);
                        Date date = null;
                        try {
                            date = parseFormat.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String timeIn24 = String.valueOf(displayFormat.format(date));
                        String hoursAndMins[] = timeIn24.split(":");

                      // Toast.makeText(context,hoursAndMins[0]+" "+hoursAndMins[1],Toast.LENGTH_SHORT).show();
                        Calendar cal = Calendar.getInstance();
                        Log.e("timecal", String.valueOf(cal.getTime().getTime()));

                        Calendar beginTime = Calendar.getInstance();
                        beginTime.set(year, month-1,day , Integer.valueOf(hoursAndMins[0]), Integer.valueOf(hoursAndMins[1]));
                        long startMillis = beginTime.getTimeInMillis();
                     //   Toast.makeText(context,String.valueOf(startMillis),Toast.LENGTH_SHORT).show();

                       // long endMillis = endTime.getTimeInMillis();


                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("beginTime", startMillis);
                        intent.putExtra("allDay", false);
                        intent.putExtra("rrule", "FREQ=DAILY");
                        intent.putExtra("endTime", startMillis+60*60*1000);
                        intent.putExtra("title", "Appointment With "+holder.name.getText().toString());
                        context.startActivity(intent);


                    }


                }
            });

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String curr[] = current.getStatus().split(" ");
            Date strDate = null;
            try {
                Log.e("dateover12", "in try");

                strDate = sdf.parse(curr[0]);
                Log.e("dateover12", strDate.toString() + "");

                if (new Date().after(strDate)) {
                    Log.e("dateover", strDate.toString() + "");

                    holder.acceptButton.setText("Remove Appointment");
                    holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mail = current.getMail();
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            database.getReference().child("Lawyers").child(mAuth.getCurrentUser().getEmail().replace(".", ",")).child("pending_appointments")
                                    .child(mail.replace(".", ",")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
//                        listItem.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, listItem.size());
                                }
                            });


                        }
                    });


                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


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

                        mail = current.getMail();
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference().child("Users").child(mail.replace(".", ",")).child("appointments")
                                .child(mAuth.getCurrentUser().getEmail().replace(".", ",")).child("status").getRef().setValue("0");
                        database.getReference().child("Lawyers").child(mAuth.getCurrentUser().getEmail().replace(".", ",")).child("pending_appointments")
                                .child(mail.replace(".", ",")).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "removed", Toast.LENGTH_SHORT).show();
//                        listItem.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, listItem.size());
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
        if (current.getName() != null) {
            holder.name.setText(current.getName().toUpperCase());
        }

    }


    private void setDate(final String txtDate, final String txtTime, AppointmentDataModel current) {
        mail = current.getMail();
        Log.e("LAdatasnapshot-mail_1:", mail + " " + current.getStatus());


        Toast.makeText(context, "" + txtDate + "and time is " + txtTime, Toast.LENGTH_SHORT).show();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        database.getReference().child("Lawyers").child(mAuth.getCurrentUser().getEmail().replace(".", ",")).child("pending_appointments")
                .child(mail.replace(".", ",")).child("status").getRef().setValue(txtDate + " at " + txtTime);


        database.getReference().child("Users").child(mail.replace(".", ",")).child("appointments")
                .child(mAuth.getCurrentUser().getEmail().replace(".", ",")).child("status").getRef().setValue(txtDate + " at " + txtTime);


    }

    private void mailLawyer(AppointmentDataModel current) {
        String email = current.getMail();
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));

        context.startActivity(Intent.createChooser(emailIntent, "Choose app to mail Lawyer"));

    }

    private void callLawyer(AppointmentDataModel current) {
        String contact = current.getNumber();
        if (contact != null) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.trim()));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
            } else {
                context.startActivity(intent);
            }
        } else {
            Toast.makeText(context, "Lawyer hasn't shared his/her contact", Toast.LENGTH_SHORT).show();
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