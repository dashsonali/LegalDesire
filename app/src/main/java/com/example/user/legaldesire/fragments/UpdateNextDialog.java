package com.example.user.legaldesire.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UpdateNextDialog extends AppCompatDialogFragment {
    Context mContext;
    Button submitBtn,nexDateBtn;
    EditText entNextDate;
    String txtDate;

    final Calendar calendar=Calendar.getInstance();
    int mYear = calendar.get(Calendar.YEAR);
    int   mMonth = calendar.get(Calendar.MONTH);
    int  mDay = calendar.get(Calendar.DAY_OF_MONTH);
    ImageButton cross;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.update_next_dialog,null);
        builder.setView(view);
        entNextDate = view.findViewById(R.id.entNextDate);
        entNextDate.setEnabled(false);
        submitBtn = view.findViewById(R.id.submit_btn);
        nexDateBtn = view.findViewById(R.id.next_datepicker);
        nexDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                entNextDate.setText( txtDate);




                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        cross = view.findViewById(R.id.closebtn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(entNextDate.getText()))
                {
                    Toast.makeText(mContext,"Please choose a date",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(getArguments()!=null)
                {
                    String key = getArguments().getString("key","");
                    String type = getArguments().getString("type","");
                    updateDatabaseAndSetReminder(key,type);


                }
                else{
                    Toast.makeText(mContext,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return  builder.create();

    }
    public void updateDatabaseAndSetReminder(String key,String type)
    {
        DatabaseReference databaseReference = null;
        if(type.equals("user"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child("cases").child(key);
        }else{
            databaseReference = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child("Lawyers").child("cases").child(key);
        }
        databaseReference.child("next_date").setValue(entNextDate.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String arr1[] = entNextDate.getText().toString().split("-");
                int day = Integer.valueOf(arr1[0]);
                int month = Integer.valueOf(arr1[1]);
                int year = Integer.valueOf(arr1[2]);
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(year, month-1,day , 0, 0);
                long startMillis = beginTime.getTimeInMillis();

                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", startMillis);
                intent.putExtra("allDay", true);
                intent.putExtra("rrule", "FREQ=DAILY");

                intent.putExtra("title", getArguments().getString("casetitle"));
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
