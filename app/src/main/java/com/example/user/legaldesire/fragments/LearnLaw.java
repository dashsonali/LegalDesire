package com.example.user.legaldesire.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.adapters.ViewPostAdapter;
import com.example.user.legaldesire.models.YouTubeDataModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class LearnLaw extends Fragment {

    private String GOOLGE_YOUTUBE_API_KEY = "AIzaSyCYW4OFJlSr5Wvwclm0yQPy_3T-M3SUx7U";
    private String CHANNEL_ID = "UCaS5ZPcMLAVPhB-54UYhoRw";
    private String CHANNEL_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId="+CHANNEL_ID+"&maxResults=20&key="+GOOLGE_YOUTUBE_API_KEY+"";
    // TODO: Rename and change types and number of parameters
    private RecyclerView mVideo_list=null;
    private ArrayList<YouTubeDataModel> mListData = new ArrayList<>();
    private ViewPostAdapter mPostAdapter = null;

    public LearnLaw(){

    }//

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn_law, container, false);
        mVideo_list = view.findViewById(R.id.video_list);
        initData(mListData);
        new RequestYoutubeAPI().execute();
        return view;
    }
    public void initData(ArrayList<YouTubeDataModel> mList){

        mVideo_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPostAdapter = new ViewPostAdapter(mList,getActivity());
        mVideo_list.setAdapter(mPostAdapter);



    }
    private  class  RequestYoutubeAPI extends AsyncTask<Void,String,String>{

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response!=null)
            {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("JSON OBJECT",jsonObject.toString());
                    mListData = parseVideoDataFromJson(jsonObject);
                    mPostAdapter.notifyDataSetChanged();
                    initData(mListData);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(CHANNEL_GET_URL);
            Log.e("URL",CHANNEL_GET_URL);

            try {

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                String json = EntityUtils.toString(httpEntity);

                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }




            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

    private ArrayList<YouTubeDataModel> parseVideoDataFromJson(JSONObject jsonObject) {
        ArrayList<YouTubeDataModel> mList = new ArrayList<>();
        if(jsonObject.has("items"))
        {
            try{
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for(int i = 0 ;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if(jsonObject1.has("id"))
                    {
                        JSONObject jObj = jsonObject1.getJSONObject("id");
                        if(jObj.has("kind")&&jObj.getString("kind").equals("youtube#video"))
                        {
                            String video_id = jObj.getString("videoId");
                            JSONObject snippet = jsonObject1.getJSONObject("snippet");
                            String title = snippet.getString("title");
                            String description = snippet.getString("description");

                            String formatedPublishedAt = formatePublishedAt(snippet.getString("publishedAt"));

                            String thumbnail = snippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");
                            YouTubeDataModel youTube = new YouTubeDataModel(title,description,thumbnail,formatedPublishedAt,video_id);
                            mList.add(youTube);

                        }
                    }
                }


            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return  mList;
    }
    public String formatePublishedAt(String publishedAt){
        String formatedPublishedAt = publishedAt.substring(0,publishedAt.lastIndexOf("T"));
        String year = formatedPublishedAt.substring(0,formatedPublishedAt.indexOf("-"));
        String date = formatedPublishedAt.substring(formatedPublishedAt.lastIndexOf("-")+1);
        String month = formatedPublishedAt.substring(formatedPublishedAt.indexOf("-")+1,formatedPublishedAt.lastIndexOf("-"));
        Log.e("month",month);
        Log.e("year",year);
        Log.e("date",date);
        switch (month)
        {
            case "01":
                month = "January";
                break;
            case "02":
                month = "February";
                break;
            case "03":
                month = "March";
                break;
            case "04":
                month = "April";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "June";
                break;
            case "07":
                month = "July";
                break;
            case "08":
                month = "August";
                break;
            case "09":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;

        }

        return date+" "+month+" "+year;

    }


}