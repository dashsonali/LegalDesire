package com.official.user.legaldesire.adapters;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.official.user.legaldesire.MainActivity;

import com.official.user.legaldesire.R;
import com.official.user.legaldesire.fragments.HomeFragment;
import com.official.user.legaldesire.models.BookmarkDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class BookmarksRecyclerAdapter extends  RecyclerView.Adapter<BookmarksRecyclerAdapter.MyViewHolder> {
    List<BookmarkDataModel> bookmarkLinks;
    Context context;
    public BookmarksRecyclerAdapter(List<BookmarkDataModel> bookmarkLinks,Context context){
        this.bookmarkLinks = bookmarkLinks;
        this.context = context;
        Toast.makeText(context,String.valueOf(bookmarkLinks.size()),Toast.LENGTH_SHORT).show();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_bookmark,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BookmarkDataModel bookmarkDataModel = bookmarkLinks.get(position);
        Log.e("Links",bookmarkDataModel.getKey()+" : "+bookmarkDataModel.getLink()+": "+bookmarkDataModel.getTitle());

       // Toast.makeText(context,link,Toast.LENGTH_SHORT).show();
        holder.title.setText(bookmarkDataModel.getTitle());
        holder.link.setText(bookmarkDataModel.getLink());
        holder.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeFragment(bookmarkDataModel,view);
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeFragment(bookmarkDataModel,view);

            }
        });
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Called","deleteBookmark");
                deleteBookmark(bookmarkDataModel,position);

            }
        });
    }
    public void deleteBookmark(final BookmarkDataModel bookmarkDataModel,final int position)
    {
        Log.e("Deleting",bookmarkDataModel.getKey());
        if(context.getSharedPreferences("MyPref", Context.MODE_PRIVATE).getString("type","").equals("lawyer"))
        {
            FirebaseDatabase.getInstance()
                    .getReference().child("Lawyers").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child("bookmarks")
                    .child(bookmarkDataModel.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(context,"Bookmark Removed",Toast.LENGTH_SHORT);
                        //bookmarkLinks.remove()
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,bookmarkLinks.size());

                    }else
                    {
                        Log.e("ERROR DELETING",task
                                .getException().toString());
                    }
                }
            });
        }else{
            FirebaseDatabase.getInstance()
                    .getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child("bookmarks")
                    .child(bookmarkDataModel.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(context,"Bookmark Removed",Toast.LENGTH_SHORT);
                        //bookmarkLinks.remove()
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,bookmarkLinks.size());

                    }else
                    {
                        Log.e("ERROR DELETING",task
                                .getException().toString());
                    }
                }
            });
        }

    }
    public void goToHomeFragment(BookmarkDataModel bookmarkDataModel,View view)
    {
        MainActivity activity = (MainActivity)view.getContext();
        String link = bookmarkDataModel.getLink();
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URL",link);
        homeFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).addToBackStack(null).commit();
    }
    @Override
    public int getItemCount() {
        return bookmarkLinks.size();
    }
    public class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView link,title;
        ImageView cross;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            link = itemView.findViewById(R.id.link);
            cross = itemView.findViewById(R.id.cross);
        }
    }
}
