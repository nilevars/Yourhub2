package com.example.ae.yourhub;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView=null;
    TextView textView=null;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        LinearLayout classified=(LinearLayout) findViewById(R.id.classified);
        classified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTint(1);
                ClassifiedHomePage myf=new ClassifiedHomePage();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content, myf);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,ClassifiedsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        LinearLayout news=(LinearLayout) findViewById(R.id.news);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTint(2);
                NewsHomePage myf=new NewsHomePage();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content, myf);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,NewsPostActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        LinearLayout forum=(LinearLayout) findViewById(R.id.forum);
        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTint(3);
                ForumHomePage myf = new ForumHomePage();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content, myf);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,PostImageActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        LinearLayout event=(LinearLayout) findViewById(R.id.event);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTint(4);
                EventHomePage myf = new EventHomePage();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content, myf);
                transaction.commit();
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(MainActivity.this,EventsActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }
    public void setTint(int n)
    {

        switch(n)
        {
            case 1:
                if(imageView!=null)
                {
                    imageView.setColorFilter(getResources().getColor(R.color.black));
                    textView.setTextColor(getResources().getColor(R.color.black));
                }

                imageView=(ImageView) findViewById(R.id.classified_icon);
                textView=(TextView) findViewById(R.id.classified_title);
                imageView.setColorFilter(getResources().getColor(R.color.colorAccent));
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case 2:
                if(imageView!=null)
                {
                    imageView.setColorFilter(getResources().getColor(R.color.black));
                    textView.setTextColor(getResources().getColor(R.color.black));
                }

                imageView=(ImageView) findViewById(R.id.news_icon);
                textView=(TextView) findViewById(R.id.news_title);
                imageView.setColorFilter(getResources().getColor(R.color.colorAccent));
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case 3:
                if(imageView!=null)
                {
                    imageView.setColorFilter(getResources().getColor(R.color.black));
                    textView.setTextColor(getResources().getColor(R.color.black));
                }

                imageView=(ImageView) findViewById(R.id.forum_icon);
                textView=(TextView) findViewById(R.id.forum_title);
                imageView.setColorFilter(getResources().getColor(R.color.colorAccent));
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case 4:
                if(imageView!=null)
                {
                    imageView.setColorFilter(getResources().getColor(R.color.black));
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                imageView=(ImageView) findViewById(R.id.event_icon);
                textView=(TextView) findViewById(R.id.event_title);
                imageView.setColorFilter(getResources().getColor(R.color.colorAccent));
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
                break;

        }
    }

}
