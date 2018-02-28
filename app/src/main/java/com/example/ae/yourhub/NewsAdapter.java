package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
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

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<NewsData> newsDataList;
    Context context;
    View rowView;
    ArrayList<HashMap<String, Object>> result;
    Drawable drw;
    private static LayoutInflater inflater=null;
    public NewsAdapter(Context mainActivity,List<NewsData> newsDataList) {
        // TODO Auto-generated constructor stub
        this.newsDataList=newsDataList;
        context=mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView user_title;
        TextView news_link;
        TextView news_desc;
        ImageView news_image;
        TextView news_title;

        LinearLayout linearLayout;
        public ViewHolder(View rowView) {
            super(rowView);
            user_title=(TextView) rowView.findViewById(R.id.title_news);
            news_title=(TextView) rowView.findViewById(R.id.title);
            news_link=(TextView) rowView.findViewById(R.id.link_news);
            news_desc=(TextView) rowView.findViewById(R.id.description);
            news_image=(ImageView) rowView.findViewById(R.id.photo);
            linearLayout=(LinearLayout) rowView.findViewById(R.id.ad_layout);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = inflater.inflate(R.layout.news_card2, parent,false);
        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.news_title.setText(newsDataList.get(position).getTitle());
        holder.user_title.setText(newsDataList.get(position).getTitle_news());
        holder.news_desc.setText(newsDataList.get(position).getDesc());
        holder.news_link.setText(newsDataList.get(position).getLink());

        Picasso.with(context).load(newsDataList.get(position).getImage()).resize(400,400).centerCrop().into(holder.news_image, new Callback()
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
                Intent intent=new Intent(context,NewsDisplayActivity.class);
                intent.putExtra("title",newsDataList.get(position).getTitle());
                intent.putExtra("link", newsDataList.get(position).getLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return newsDataList.size();
    }

}
