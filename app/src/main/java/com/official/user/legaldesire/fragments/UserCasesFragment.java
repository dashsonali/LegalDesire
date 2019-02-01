package com.official.user.legaldesire.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.official.user.legaldesire.R;
import com.official.user.legaldesire.adapters.CasesAdapter;
import com.official.user.legaldesire.models.CasesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserCasesFragment extends Fragment {
    Context mContext;
    List<CasesModel> casesModelList = new ArrayList<>();
    CasesAdapter casesAdapter ;
    RecyclerView manualCases;
    FloatingActionButton addCases;
    ProgressBar mProgressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_cases, container, false);
        addCases = view.findViewById(R.id.addCases);
        manualCases = view.findViewById(R.id.manualCase);
        manualCases.setLayoutManager(new LinearLayoutManager(mContext));
        mProgressBar = view.findViewById(R.id.mProgressBar);
        addCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CasesDialog casesDialog = new CasesDialog();
                casesDialog.show(getChildFragmentManager(),"user");

            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(isVisibleToUser)
        {
            loadRecyclerView();
        }
    }
    public void loadRecyclerView(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail()
                .replace(".",","));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                casesModelList.clear();


                mProgressBar.setVisibility(View.GONE);
                if(dataSnapshot.hasChild("cases")){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.child("cases").getChildren())
                    {
                       // Toast.makeText(mContext,dataSnapshot1.child("oppositon_party").getValue().toString(),Toast.LENGTH_SHORT).show();
                        CasesModel casesModel = new CasesModel(dataSnapshot1.child("casename").getValue().toString(),
                                dataSnapshot1.child("oppositon_party").getValue().toString(),
                                dataSnapshot1.child("court").getValue().toString(),
                                dataSnapshot1.child("lawyer").getValue().toString(),
                                dataSnapshot1.child("date_of_initation").getValue().toString(),
                                dataSnapshot1.child("next_date").getValue().toString(),
                                dataSnapshot1.child("key").getValue().toString());
                        casesModelList.add(casesModel);
                        casesAdapter = new CasesAdapter(casesModelList,mContext);
                        manualCases.setAdapter(casesAdapter);
                        casesAdapter.notifyDataSetChanged();


                    }

                }else{
                    Toast.makeText(mContext,"You haven't added any cases",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
