package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by A E on 22-Jun-17.
 */

public class AdDisplay extends Fragment {
    Context context;
    TextView title,desc,price,location;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.classified_card, container, false);
        title = (TextView) view.findViewById(R.id.ad_title);
        price = (TextView) view.findViewById(R.id.ad_price);
        desc= (TextView) view.findViewById(R.id.ad_desc);
        location = (TextView) view.findViewById(R.id.ad_loc);
        ImageView img = (ImageView) view.findViewById(R.id.ad_photo);

        desc.setVisibility(View.VISIBLE);
        location.setVisibility(View.VISIBLE);

        title.setText(getArguments().getString("title"));
        price.setText(getArguments().getString("price"));
        desc.setText(getArguments().getString("desc"));
        location.setText(getArguments().getString("location"));

        Picasso.with(context).load(getArguments().getString("img")).resize(600, 500).centerCrop().into(img, new Callback() {
            @Override
            public void onSuccess(){
                System.out.println("loaded Image");
            }

            @Override
            public void onError(){
                System.out.println("Unable to load Image");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Ad");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}