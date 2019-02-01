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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.official.user.legaldesire.R;


public class LegalDraftingFragment extends AppCompatDialogFragment{
    Context mContext;
    Spinner legalCatagorySpinner;
    EditText name,contact,contactEmail;
    Button sendBtn;
    ImageButton cross;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.fragment_legal_drafting,null);
        builder.setView(view);
        legalCatagorySpinner = view.findViewById(R.id.categorySpinner);
        String legalCatagory[] = getResources().getStringArray(R.array.legal_category);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item,legalCatagory);
        legalCatagorySpinner.setAdapter(spinnerAdapter);
        name = view.findViewById(R.id.entName);
        contact = view.findViewById(R.id.entContact);
        contactEmail = view.findViewById(R.id.entEmail);
        sendBtn = view.findViewById(R.id.submit_btn);cross = view.findViewById(R.id.closebtn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(legalCatagorySpinner.getSelectedItem().toString().equals("Select Category")){
                    Toast.makeText(mContext,"Please select a category!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(name.getText()))
                {
                    name.setError("Enter your name!");
                    name.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(contact.getText())){
                    contact.setError("Enter your contact!");
                    contact.requestFocus();
                    return;
                }if(TextUtils.isEmpty(contactEmail.getText()))
                {
                    contactEmail.setError("Enter you contact email!");
                    contactEmail.requestFocus();
                }
                sendMailWithInformation(name.getText().toString(),contact.getText().toString(),legalCatagorySpinner.getSelectedItem().toString()
                        ,contactEmail.getText().toString());

            }
        });

        return  builder.create();
    }
    public void sendMailWithInformation(String name,String contact,String category,String contactEmail)
    {
        String[] TO = {"swain.rakesh131@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Service Lead");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Name : "+name+"\n"+"Contact : "+contact+"\n"+"Category : "+category+"\n"+"Contact Email : "+contactEmail);
        try{
            startActivityForResult(Intent.createChooser(emailIntent,"Send email.."),10);

        }catch (ActivityNotFoundException e)
        {
            Toast.makeText(mContext,"No email client found.",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
}
