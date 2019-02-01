package com.official.user.legaldesire.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.official.user.legaldesire.R;
import com.official.user.legaldesire.adapters.BookmarksRecyclerAdapter;
import com.official.user.legaldesire.models.BookmarkDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BookmarkFragment extends Fragment {

    RecyclerView bookmarkRecycler;
    RecyclerView.Adapter bookmarksRecyclerAdapter;
    Context mContext;
    public BookmarkFragment() {
        // Required empty public constructor
    }
    List<BookmarkDataModel> bookmarkLinks = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getContext(),"SHOW",Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        bookmarkRecycler = view.findViewById(R.id.bookmarkRecycler);
        bookmarkRecycler.setHasFixedSize(true);
        bookmarkRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        loadData();
        return view;
    }




    public void loadData(){
        FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = fdatabase.getReference().child("Lawyers").child(FirebaseAuth.getInstance()
        .getCurrentUser().getEmail().replace(".",","));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookmarkLinks.clear();
                if(!dataSnapshot.hasChild("bookmarks"))
                {
                    Toast.makeText(mContext,"You haven't added any bookmarks!",Toast.LENGTH_SHORT).show();
                }else
                {
                    for(DataSnapshot dataSnapshot1 :dataSnapshot.child("bookmarks").getChildren()){
                        Log.e("coming here",dataSnapshot1.toString());
                        if(dataSnapshot1.hasChild("link")&&dataSnapshot1.hasChild("title"))
                        {
                            BookmarkDataModel bookmarkDataModel = new BookmarkDataModel(dataSnapshot1.getKey(),dataSnapshot1.child("link").getValue().toString(),
                                    dataSnapshot1.child("title").getValue().toString());
                            bookmarkLinks.add(bookmarkDataModel);
                            //bookmarkLinks.add(dataSnapshot1.getValue().toString());
                            bookmarksRecyclerAdapter = new BookmarksRecyclerAdapter(bookmarkLinks,getContext());
                            bookmarkRecycler.setAdapter(bookmarksRecyclerAdapter);
                        }else{
                            Toast.makeText(mContext,"Wait for a few seconds",Toast.LENGTH_SHORT).show();
                        }

                    }


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
