package com.example.user.legaldesire.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.legaldesire.R;




public class FileRtiFragment extends AppCompatDialogFragment {
    Context mContext;
    EditText entName,entAddress,entInfo,entContact;
    Button sendBtn;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.fragment_file_rti,null);
        builder.setView(view);
        entName = view.findViewById(R.id.entName);
        entAddress = view.findViewById(R.id.entAddress);
        entContact = view.findViewById(R.id.entContact);
        entInfo = view.findViewById(R.id.entInformation);
        sendBtn = view.findViewById(R.id.submit_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(entName.getText()))
                {
                    entName.setError("Enter your name!");
                    entName.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(entAddress.getText()))
                {
                    entAddress.setError("Enter your address!");
                    entAddress.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(entContact.getText())){
                    entContact.setError("Enter your contact!");
                    entContact.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(entInfo.getText()))
                {
                    entInfo.setError("Enter your questions/information you seek!");
                    entInfo.requestFocus();
                    return;
                }

                sendMailWithInformation(entName.getText().toString(),entContact.getText().toString(),entAddress.getText().toString(),
                        entInfo.getText().toString());
            }
        });
        return  builder.create();
    }
    public void sendMailWithInformation(String name,String contact,String address,String info){
        String[] TO = {"swain.rakesh131@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Service Lead");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Name :"+name+"\n"+"Contact :"+contact+"\n"+"Address :"+address+"\n"+"\n"+info);
        try{
            startActivityForResult(Intent.createChooser(emailIntent,"Send email.."),10);

        }catch (ActivityNotFoundException e)
        {
            Toast.makeText(mContext,"No email client found.",Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10)
        {

           Toast toast =  Toast.makeText(mContext,"Your request for service is received\n Our Representative will contact you shortly"
            ,Toast.LENGTH_LONG);
           toast.setGravity(Gravity.CENTER,0,0);
           toast.show();
            dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
