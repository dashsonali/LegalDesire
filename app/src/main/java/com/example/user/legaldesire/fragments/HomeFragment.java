package com.example.user.legaldesire.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;


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
import android.widget.ProgressBar;

import com.example.user.legaldesire.R;


public class HomeFragment extends Fragment {
    private WebView webView;

    ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private View rootView;
    private Bundle webviewBundle;

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
            progressBar = rootView.findViewById(R.id.progressBar);
            progressBar.setMax(100);
            if(savedInstanceState!=null)
            {
                webView.restoreState(webviewBundle);
                Log.e("Saved instance","NOT NULL");
            }else{
                WebSettings webSettings=webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                Log.e("Saved instance","NULL");
                webView.loadUrl("https://legaldesire.com/");
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



        return rootView;

    }

    @Override
    public void onPause() {
        super.onPause();
        webviewBundle = new Bundle();
        webView.saveState(webviewBundle);
    }
}


