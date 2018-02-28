package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by A E on 13-Jul-17.
 */

public class UserDetails extends Fragment {
    Context context;
    SharedPreferences hub;
    TextView name;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.user_card, container, false);
        name = (TextView) view.findViewById(R.id.username);

        hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
        String name1=hub.getString("username","");
        name.setText(name1);


        Button logout=(Button) view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hub.edit().putString("username","").apply();
                hub.edit().putString("userid","").apply();
                Intent intent=new Intent(getActivity(),UserActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User");

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }
}
