package com.official.user.legaldesire.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.official.user.legaldesire.R;


public class HomeFragment extends Fragment {
    private WebView webView;
    private String title="untitled";
    ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private View rootView;
    private Bundle webviewBundle;
    private ImageView bookMarkBtn;
    private String URL = "";
    Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(rootView==null)
        {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
            webView=rootView.findViewById(R.id.webview);
          //  progressDialog = ProgressDialog.show(getContext(), "", "Please wait, our page is loading", true);
            bookMarkBtn = rootView.findViewById(R.id.bookMarkBtn);
            progressBar = rootView.findViewById(R.id.progressBar);
            bookMarkBtn.setVisibility(View.INVISIBLE);
            progressBar.setMax(100);
            if(savedInstanceState!=null)
            {
                webView.restoreState(webviewBundle);
                Log.e("Saved instance","NOT NULL");
            }else{
                WebSettings webSettings=webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                Log.e("Saved instance","NULL");

                if(getArguments()!=null)
                {
                    URL = getArguments().getString("URL");
                }else
                {
                    URL = "https://legaldesire.com/";
                }
                webView.loadUrl(URL);
                webView.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        progressBar.setProgress(newProgress);
                        if(newProgress == 100)
                        {
                            progressBar.setVisibility(View.GONE);

                        }

                    }

                });


                webView.setWebViewClient(new WebViewClient() {



                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        bookMarkBtn.setVisibility(View.VISIBLE);
                        title = view.getTitle();
                    }
                });

                webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
                webView.setHorizontalScrollBarEnabled(false);
                webView.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if(keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                        {
                            WebView webView = (WebView) view;

                            switch(i)
                            {
                                case KeyEvent.KEYCODE_BACK:
                                    if(webView.canGoBack())
                                    {
                                        webView.goBack();
                                        return true;
                                    }
                                    break;
                            }}
                        return false;
                    }
                });








            }

        }

        bookMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"ADDING BOOKMARK",Toast.LENGTH_SHORT).show();
                addBookMark(webView.getUrl(),title);
            }
        });
        return rootView;

    }

    public void addBookMark(final String url,final String title){
        Toast.makeText(mContext,"ADDING BOOKMARK",Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final DatabaseReference databaseReference;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(sharedPreferences.getString("type","user").equals("user"))
        {
           // Toast.makeText(getContext(),"USER TYPE USER",Toast.LENGTH_SHORT).show();
            databaseReference = database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child("bookmarks");
        }else{

            databaseReference = database.getReference().child("Lawyers").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",",")).child("bookmarks");

        }
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(getContext(),"comming here"+String.valueOf(dataSnapshot.getChildrenCount()),Toast.LENGTH_SHORT).show();
                if(dataSnapshot.getChildrenCount()==0)
                {
                    String key = databaseReference.push().getKey();
                    databaseReference.child(key).child("link").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(mContext,"Completed!",Toast.LENGTH_SHORT).show();
                            if(task.isSuccessful())
                            {

                                Toast.makeText(mContext,"Bookmark Added!",Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Log.e("bookmark error",task.getException().toString());
                                Toast.makeText(mContext,task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    databaseReference.child(key).child("title").setValue(title).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(mContext,"Completed!",Toast.LENGTH_SHORT).show();
                            if(task.isSuccessful())
                            {

                                Toast.makeText(mContext,"Bookmark Added!",Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Log.e("bookmark error",task.getException().toString());
                                Toast.makeText(mContext,task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    boolean bookmarkExists=false;
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        if(dataSnapshot1.child("link").getValue().toString().equals(url))
                        {
                            Toast.makeText(mContext,"Bookmark Already Exists!",Toast.LENGTH_SHORT).show();
                            bookmarkExists=true;
                        }
                    }
                    if(!bookmarkExists)
                    {

                        String key = databaseReference.push().getKey();
                        databaseReference.child(key).child("link").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(mContext,"Completed!",Toast.LENGTH_SHORT).show();
                                if(task.isSuccessful())
                                {

                                    Toast.makeText(mContext,"Bookmark Added!",Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    Log.e("bookmark error",task.getException().toString());
                                    Toast.makeText(mContext,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });databaseReference.child(key).child("title").setValue(title).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(mContext,"Completed!",Toast.LENGTH_SHORT).show();
                                if(task.isSuccessful())
                                {

                                    Toast.makeText(mContext,"Bookmark Added!",Toast.LENGTH_SHORT).show();
                                }else
                                {
                                    Log.e("bookmark error",task.getException().toString());
                                    Toast.makeText(mContext,task.getException().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
    @Override
    public void onPause() {
        super.onPause();
        webviewBundle = new Bundle();
        webView.saveState(webviewBundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}


