package com.example.user.legaldesire.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.models.CasesModel;

import java.util.List;

public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.MyViewHolder> {
    Context mContext;
    List<CasesModel> itemList;
    public CasesAdapter(List<CasesModel> itemList,Context mContext){
        this.itemList = itemList;
        this.mContext = mContext;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cases_cell,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final CasesModel currentCase = itemList.get(position);
        holder.caseName.setText(currentCase.getCasename());
        holder.courtName.setText("Court: "+currentCase.getCourt());
        holder.lawyer.setText("Lawyer: "+currentCase.getLawyer());
        holder.initiationdate.setText("Initiation Date: "+currentCase.getDate_of_initation());
        holder.nextdate.setText("Next Date: "+currentCase.getNext_date());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courtName,caseName,opposition_party,initiationdate,nextdate,lawyer;
        public MyViewHolder(View itemView) {
            super(itemView);
            courtName = itemView.findViewById(R.id.court_name);
            caseName = itemView.findViewById(R.id.case_name);
            opposition_party = itemView.findViewById(R.id.oppositon_party);
            initiationdate = itemView.findViewById(R.id.initiation_date);
            nextdate =itemView.findViewById(R.id.next_date);
            lawyer = itemView.findViewById(R.id.lawyer_name);

        }
    }
}
