package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by A E on 29-Jun-17.
 */

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    List<CommunityData> communityDatas;
    Context context;
    View rowView;
    ArrayList<HashMap<String, Object>> result;
    Drawable drw;
    private static LayoutInflater inflater=null;
    public CommunityAdapter(Context mainActivity,List<CommunityData> communityDatas) {
        // TODO Auto-generated constructor stub
        this.communityDatas=communityDatas;
        context=mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView news_title;
        TextView news_link;
        LinearLayout linearLayout;
        public ViewHolder(View rowView) {
            super(rowView);
            news_title=(TextView) rowView.findViewById(R.id.title_news);
            news_link=(TextView) rowView.findViewById(R.id.link_news);
            linearLayout=(LinearLayout) rowView.findViewById(R.id.ad_layout);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = inflater.inflate(R.layout.community_card,  parent,false);
        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.news_title.setText(communityDatas.get(position).getName());
        holder.news_link.setText(communityDatas.get(position).getId());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                hub.edit().putString("comm_id",communityDatas.get(position).getId()).commit();
                Log.i("comm_id",hub.getString("comm_id",""));
                hub.edit().putString("community",communityDatas.get(position).getName()).commit();
                Intent intent=new Intent(context,HomeActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return communityDatas.size();
    }
}
