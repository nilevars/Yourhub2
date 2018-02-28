package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    ImageView home,comm,msg,user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        SharedPreferences hub=this.getSharedPreferences("com.example.ae.yourhub", Context.MODE_PRIVATE);
        String user_id=hub.getString("userid","");
        if((user_id.matches("")))
        {
            Intent intent=new Intent(this,UserActivity.class);
            startActivity(intent);
        }

        final FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        home=(ImageView) findViewById(R.id.home_icon);
        comm=(ImageView) findViewById(R.id.community_icon);
        msg=(ImageView) findViewById(R.id.message_icon);
        user=(ImageView) findViewById(R.id.user_icon);



        home.setColorFilter(getResources().getColor(R.color.pink_400));
        HomePage myf=new HomePage();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.add(R.id.content, myf);
        transaction.commit();

        comm.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v){
                setBlack();
                comm.setColorFilter(getResources().getColor(R.color.blue_400));
                CommunityFragment myf2 = new CommunityFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.add(R.id.content, myf2);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(HomeActivity.this,CreateCommunityActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                setBlack();
                home.setColorFilter(getResources().getColor(R.color.pink_400));
                HomePage myf=new HomePage();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.add(R.id.content, myf);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(HomeActivity.this,CreateCommunityActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                setBlack();
                user.setColorFilter(getResources().getColor(R.color.red_300));
                UserDetails myf=new UserDetails();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addToBackStack(null);
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.add(R.id.content, myf);
                transaction.commit();
            }
        });


    }
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
    void setBlack()
    {
        home.setColorFilter(getResources().getColor(R.color.black));
        comm.setColorFilter(getResources().getColor(R.color.black));
        msg.setColorFilter(getResources().getColor(R.color.black));
        user.setColorFilter(getResources().getColor(R.color.black));
    }

}
