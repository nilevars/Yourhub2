package com.example.ae.yourhub;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by A E on 22-Jun-17.
 */

public class ClassifiedsAdapter extends RecyclerView.Adapter<ClassifiedsAdapter.ViewHolder> {
    List<ClassifiedData> classifiedDataList;
    Context context;
    View rowView;
    private final static int FADE_DURATION = 1000;
    private static LayoutInflater inflater=null;

    public ClassifiedsAdapter(Context mainActivity,List<ClassifiedData> classifiedDataList) {
        // TODO Auto-generated constructor stub
        this.classifiedDataList=classifiedDataList;
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
        holder.ad_title.setText(classifiedDataList.get(position).getTitle());
        holder.l.setText(classifiedDataList.get(position).getLocation());
        holder.price.setText(classifiedDataList.get(position).getPrice());
        if()
        Picasso.with(context).load(classifiedDataList.get(position).getImage()).resize(400,400).centerCrop().into(holder.img, new Callback()
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
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("title",classifiedDataList.get(position).getTitle());
                bundle.putString("img", classifiedDataList.get(position).getImage());
                bundle.putString("desc",classifiedDataList.get(position).getDescription());
                bundle.putString("price",classifiedDataList.get(position).getPrice());
                bundle.putString("location",classifiedDataList.get(position).getLocation());


                Fragment mFragment = new AdDisplay();
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction ft=fragmentManager.beginTransaction().addToBackStack(null);
                mFragment.setArguments(bundle);
                ft.replace(R.id.content, mFragment);
                ft.commit();
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
        return classifiedDataList.size();
    }
}
