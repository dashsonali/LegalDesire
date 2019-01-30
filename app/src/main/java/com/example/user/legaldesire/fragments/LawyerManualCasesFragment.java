package com.example.user.legaldesire.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.legaldesire.R;


public class LawyerManualCasesFragment extends Fragment {
  Context mContext;

    FloatingActionButton addCases;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_lawyer_manual_cases, container, false);
        addCases = view.findViewById(R.id.addCases);
        addCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CasesDialog casesDialog = new CasesDialog();
                casesDialog.show(getChildFragmentManager(),"lawyer");

            }
        });
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext  = context;


    }


}
