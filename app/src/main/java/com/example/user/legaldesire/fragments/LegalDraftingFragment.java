package com.example.user.legaldesire.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.user.legaldesire.R;


public class LegalDraftingFragment extends AppCompatDialogFragment{
    Context mContext;
    Spinner legalCatagorySpinner;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.fragment_legal_drafting,null);
        builder.setView(view);
        String legalCatagory[] = getResources().getStringArray(R.array.legal_category);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item,legalCatagory);
        legalCatagorySpinner.setAdapter(spinnerAdapter);
        return  builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
