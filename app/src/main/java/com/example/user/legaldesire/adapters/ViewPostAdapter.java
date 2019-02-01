package com.example.user.legaldesire.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.legaldesire.R;
import com.example.user.legaldesire.VideoPlayActivity;
import com.example.user.legaldesire.models.YouTubeDataModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPostAdapter extends RecyclerView.Adapter<ViewPostAdapter.YoutubePostViewHolder> {
    public ArrayList<YouTubeDataModel> dataset;
    public Context mContext =null;
    private  String video_id;
    private InterstitialAd mInterstitialAd;


    public ViewPostAdapter(ArrayList<YouTubeDataModel> dataset, Context mContext) {
        this.dataset = dataset;
        this.mContext = mContext;
    }

    @Override
    public YoutubePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_post_layotu,parent,false);
        YoutubePostViewHolder youtubePostViewHolder = new YoutubePostViewHolder(view);
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId("ca-app-pub-1918084302457714/8503055917");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("8B6394015EB78261F108E7DD36ED7498").build());

        return youtubePostViewHolder;
    }

    @Override
    public void onBindViewHolder(YoutubePostViewHolder holder, int position) {
            final YouTubeDataModel object = dataset.get(position);

            holder.textViewTitle.setText(object.getTitle());
            holder.textViewDate.setText(object.getPublishedAt());
           // holder.textViewDes.setText(object.getDes());

             Picasso.get().load(object.getThumb()).into(holder.imageViewThumb);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 goToPlayerView(object);
             }
         });

            //TODO:Image will be downloaded from URL

    }
    public void goToPlayerView(final YouTubeDataModel object)
    {
        if(mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    Intent intent = new Intent(mContext, VideoPlayActivity.class);
                    intent.putExtra("video_id",object.getVideo_id());
                    mContext.startActivity(intent);
                }
            });
        }else{
            Intent intent = new Intent(mContext, VideoPlayActivity.class);
            intent.putExtra("video_id",object.getVideo_id());
            mContext.startActivity(intent);
        }

    }
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class YoutubePostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle,textViewDate,textViewDes;
        ImageView imageViewThumb;
        public YoutubePostViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.published_at);
            textViewTitle = itemView.findViewById(R.id.video_title);
           // textViewDes = itemView.findViewById(R.id.video_des);
            imageViewThumb = itemView.findViewById(R.id.thumb_image);
        }
    }
}
