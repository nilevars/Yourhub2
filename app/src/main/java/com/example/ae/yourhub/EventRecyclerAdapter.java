package com.example.ae.yourhub;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {
    List<EventData> eventDatas;
    Context context;
    View rowView;
    private final static int FADE_DURATION = 500;
    private static LayoutInflater inflater=null;

    public EventRecyclerAdapter(Context mainActivity, List<EventData> eventDatas){

        this.eventDatas=eventDatas;
        context=mainActivity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView name;
        ImageView img;
        TextView dt;
        TextView t;
        TextView l;
        LinearLayout linearLayout;
        public ViewHolder(View rowView) {
            super(rowView);
            name=(TextView) rowView.findViewById(R.id.event_name);
            l=(TextView) rowView.findViewById(R.id.event_loc);
            img=(ImageView) rowView.findViewById(R.id.event_photo);
            dt=(TextView) rowView.findViewById(R.id.event_date) ;
            t=(TextView) rowView.findViewById(R.id.event_time) ;
            linearLayout=(LinearLayout) rowView.findViewById(R.id.ad_layout);
        }
    }
    @Override
    public EventRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rowView = inflater.inflate(R.layout.event_card, null);
        ViewHolder vh = new ViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventRecyclerAdapter.ViewHolder holder, final int position){
        holder.name.setText(eventDatas.get(position).getName());
        holder.l.setText(eventDatas.get(position).getLocation());
        holder.dt.setText(eventDatas.get(position).getDate());
        holder.t.setText(eventDatas.get(position).getTime());
        Picasso.with(context).load(eventDatas.get(position).getImages()).resize(400,400).centerCrop().into(holder.img, new Callback()
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

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    @Override
    public int getItemCount() {
        return eventDatas.size();
    }
}
