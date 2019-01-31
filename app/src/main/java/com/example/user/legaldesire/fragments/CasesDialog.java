package com.example.user.legaldesire.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.models.CasesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CasesDialog extends AppCompatDialogFragment {
    private Context mContext;
    private ProgressBar mProgressBar;
    private Button submitBtn,chooseInitiationDateBtn,chooseNextDateBtn;
    private ImageButton cross;
    private DatabaseReference databaseReference;
    private  FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private   FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private EditText caseNameEditText,courtNameEditText,lawyerEditText,initiationDateEditText,nextDateEditText,oppositionPartyEditText;
    final Calendar calendar=Calendar.getInstance();
    int mYear = calendar.get(Calendar.YEAR);
    int   mMonth = calendar.get(Calendar.MONTH);
    int  mDay = calendar.get(Calendar.DAY_OF_MONTH);
    String  txtDate="date";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.cases_dialog_fragment,null);
        builder.setView(view);

        submitBtn = view.findViewById(R.id.submit_btn);

        chooseInitiationDateBtn = view.findViewById(R.id.initiation_datepicker);
        chooseNextDateBtn = view.findViewById(R.id.next_datepicker);
        caseNameEditText = view.findViewById(R.id.entCaseName);
        courtNameEditText = view.findViewById(R.id.entCourtName);
        lawyerEditText = view.findViewById(R.id.entLawyerName);
        initiationDateEditText = view.findViewById(R.id.entIntiationDate);
        nextDateEditText = view.findViewById(R.id.entNextDate);
        oppositionPartyEditText = view.findViewById(R.id.entOppostionParty);
        cross = view.findViewById(R.id.closebtn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initiationDateEditText.setEnabled(false);
        nextDateEditText.setEnabled(false);
        mProgressBar = new ProgressBar(mContext);

        if(getTag().equals("lawyer"))
            { databaseReference = firebaseDatabase.getReference().child("Lawyers").child(mAuth.getCurrentUser().getEmail().replace(".",","));
            }else{
            databaseReference = firebaseDatabase.getReference().child("Users").child(mAuth.getCurrentUser().getEmail().replace(".",","));
        }
       submitBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(TextUtils.isEmpty(caseNameEditText.getText()))
               {
                   caseNameEditText.setError("Enter case name");
                   caseNameEditText.requestFocus();
                   return;

               }
               else if(TextUtils.isEmpty(courtNameEditText.getText()))
               {
                   courtNameEditText.setError("Enter court name");
                   courtNameEditText.requestFocus();
                   return;
               }
              else if(TextUtils.isEmpty(lawyerEditText.getText()))
               {
                   lawyerEditText.setError("Enter lawyer name");
                   lawyerEditText.requestFocus();
                   return;
               }
              else if(TextUtils.isEmpty(initiationDateEditText.getText()))
               {
                   initiationDateEditText.setError("Choose an initiation date");
                   initiationDateEditText.requestFocus();
                   return;
               }else if(TextUtils.isEmpty(nextDateEditText.getText()))
               {
                   nextDateEditText.setError("Choose next date");
                   nextDateEditText.requestFocus();
                   return;
               }else if(TextUtils.isEmpty(oppositionPartyEditText.getText()))
               {
                   oppositionPartyEditText.setError("Enter opposition party");
                   oppositionPartyEditText.requestFocus();
                   return;
               }else{
                   addCase();
               }

           }
       });
        chooseNextDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                nextDateEditText.setText( txtDate);




                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        chooseInitiationDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                initiationDateEditText.setText( txtDate);




                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        return  builder.create();
    }

    public void addCase() {
        mProgressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = databaseReference.push().getKey();
                CasesModel casesModel = new CasesModel(caseNameEditText.getText().toString(),
                        oppositionPartyEditText.getText().toString(),
                        courtNameEditText.getText().toString(),
                        lawyerEditText.getText().toString(),
                        initiationDateEditText.getText().toString(),
                        nextDateEditText.getText().toString(),
                        key);
                databaseReference.child("cases").child(key).setValue(casesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mContext,"Cases added!",Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                        addReminder(nextDateEditText.getText().toString(),caseNameEditText.getText().toString());
                        dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void addReminder(String nexdate,String casename)
    {
        String arr1[] = nexdate.toString().split("-");
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

        intent.putExtra("title",casename );
        mContext.startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
