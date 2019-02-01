package com.official.user.legaldesire.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.official.user.legaldesire.R;
import com.official.user.legaldesire.fragments.UpdateNextDialog;
import com.official.user.legaldesire.models.CasesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.MyViewHolder> {
    Context mContext;
    List<CasesModel> itemList;
    SharedPreferences sharedPreferences;
    int position;
    public CasesAdapter(List<CasesModel> itemList,Context mContext){
        this.itemList = itemList;
        this.mContext = mContext;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cases_cell,parent,false);
        sharedPreferences = mContext.getSharedPreferences("MyProfile",Context.MODE_PRIVATE);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        this.position = position;
        final CasesModel currentCase = itemList.get(position);
        holder.caseName.setText(currentCase.getCasename());
        holder.courtName.setText("Court: "+currentCase.getCourt());
        holder.lawyer.setText("Lawyer: "+currentCase.getLawyer());
        holder.initiationdate.setText("Initiation Date: "+currentCase.getDate_of_initation());
        holder.nextdate.setText("Next Date: "+currentCase.getNext_date());
        holder.opposition_party.setText("Opposition Party: "+currentCase.getOppositon_party());
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNextDate(currentCase,sharedPreferences.getString("type","user"));
            }
        });
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCase(currentCase,sharedPreferences.getString("type","user"));
            }
        });

    }
    public void updateNextDate(CasesModel currentCase,String type)
    {

        UpdateNextDialog updateNextDialog = new UpdateNextDialog();
        Bundle bundle = new Bundle();
        bundle.putString("key",currentCase.getKey());
        bundle.putString("type",sharedPreferences.getString("type","user"));
        bundle.putString("casetitle",currentCase.getCasename());
        updateNextDialog.setArguments(bundle);
        updateNextDialog.show(((AppCompatActivity)mContext).getSupportFragmentManager(),type);
    }
    public void removeCase(CasesModel currentCase,String type){
        String key = currentCase.getKey();
        DatabaseReference databaseReference = null;
        if(type.equals("user"))
        {
             databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","));
        }else if(type.equals("lawyer"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Lawyers").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",","));
        }
        databaseReference.child("cases").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(mContext,"Case removed",Toast.LENGTH_SHORT).show();

                notifyItemRangeChanged(position,itemList.size());
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courtName,caseName,opposition_party,initiationdate,nextdate,lawyer;
        Button removeBtn,updateBtn;
        public MyViewHolder(View itemView) {
            super(itemView);
            courtName = itemView.findViewById(R.id.court_name);
            caseName = itemView.findViewById(R.id.case_name);
            opposition_party = itemView.findViewById(R.id.oppositon_party);
            initiationdate = itemView.findViewById(R.id.initiation_date);
            nextdate =itemView.findViewById(R.id.next_date);
            lawyer = itemView.findViewById(R.id.lawyer_name);
            removeBtn = itemView.findViewById(R.id.removeBtn);
            updateBtn = itemView.findViewById(R.id.updateNexDateBtn);

        }
    }
}
