package com.example.user.legaldesire.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.user.legaldesire.R;


public class HomeFragment extends Fragment {
    private WebView webView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        progressDialog = ProgressDialog.show(getContext(), "", "Please wait, our page is loading", true);
        //progressDialog..getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        View rootView= inflater.inflate(R.layout.fragment_home, container, false);
        webView=rootView.findViewById(R.id.webview);

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

       //webView.getSettings().setBuiltInZoomControls(true);

        webView.loadUrl("https://legaldesire.com/");

        //HELLO
        webView.setWebViewClient(new WebViewClient() {



            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 4000);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

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

        return rootView;

    }



 }


