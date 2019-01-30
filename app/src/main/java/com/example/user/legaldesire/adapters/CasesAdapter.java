package com.example.user.legaldesire.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
