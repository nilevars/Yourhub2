package com.example.ae.yourhub;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by A E on 20-Jul-17.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {
    List<DirectoryData> directoryDataList;
    Context context;
    View rowView;
    private final static int FADE_DURATION = 1000;
    private static LayoutInflater inflater=null;

    public DirectoryAdapter(Context mainActivity,List<DirectoryData> directoryDataList) {
        // TODO Auto-generated constructor stub
        this.directoryDataList=directoryDataList;
        context=mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView ad_title;
        ImageView img;
        TextView price;
        TextView l;
        LinearLayout linearLayout;
        public ViewHolder(View rowView) {
            super(rowView);
            ad_title=(TextView) rowView.findViewById(R.id.ad_title);
            l=(TextView) rowView.findViewById(R.id.ad_loc);
            img=(ImageView) rowView.findViewById(R.id.ad_photo);
            price=(TextView) rowView.findViewById(R.id.ad_price) ;
            linearLayout=(LinearLayout) rowView.findViewById(R.id.ad_layout);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = inflater.inflate(R.layout.classified_card, null);
        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.ad_title.setText(directoryDataList.get(position).getName());
        holder.l.setText(directoryDataList.get(position).getLocation());
        holder.price.setText(directoryDataList.get(position).getType());
        Picasso.with(context).load(directoryDataList.get(position).getImage()).resize(400,400).centerCrop().into(holder.img, new Callback()
        {
            @Override
            public void onSuccess()
            {
                System.out.println("loaded Image");
            }
            @Override
            public void onError()
            {
                System.out.println("Unable to load Image");
            }
        });
        setScaleAnimation(holder.linearLayout);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }
    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    @Override
    public int getItemCount(){
        return directoryDataList.size();
    }
}
