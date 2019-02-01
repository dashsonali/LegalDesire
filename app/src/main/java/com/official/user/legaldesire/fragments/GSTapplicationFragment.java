package com.official.user.legaldesire.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.official.user.legaldesire.R;


public class GSTapplicationFragment extends AppCompatDialogFragment {
    Context mContext;
    EditText name,contact,address,email,firmName;
    Button sendBtn;
    ImageButton cross;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.fragment_gstapplication,null);
        builder.setView(view);
        name = view.findViewById(R.id.entName);
        email = view.findViewById(R.id.entEmail);
        contact = view.findViewById(R.id.entContact);
        address = view.findViewById(R.id.entAddress);
        firmName = view.findViewById(R.id.entFirmName);
        sendBtn = view.findViewById(R.id.submit_btn);
        cross = view.findViewById(R.id.closebtn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(name.getText()))
                {
                    name.setError("Enter your name!");
                    name.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(address.getText()))
                {
                   address.setError("Enter your address!");
                    address.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(contact.getText())){
                   contact.setError("Enter your contact!");
                   contact.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(firmName.getText()))
                {
                    firmName.setError("Enter your Firm/Organisation name!");
                  firmName.requestFocus();
                    return;
                }if(TextUtils.isEmpty(email.getText()))
                {
                    email.setError("Enter you contact email!");
                    email.requestFocus();
                }
                sendMailWithInformation(name.getText().toString(),contact.getText().toString(),address.getText().toString(),firmName.getText().toString()
                ,email.getText().toString());
            }
        });
        return  builder.create();
    }

    public void sendMailWithInformation(String name,String contact,String address,String firmName,String email)
    {
        String[] TO = {"swain.rakesh131@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Service Lead");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Name : "+name+"\n"+"Contact : "+contact+"\n"+"Address : "+address+"\n"+"Firm/Organisation Name : "+firmName+"\n"+"Contact Email : "+email);
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
