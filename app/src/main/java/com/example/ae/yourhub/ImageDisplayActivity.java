package com.example.ae.yourhub;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageDisplayActivity extends AppCompatActivity {

    LinearLayout l1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Article");
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_clear);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        l1=(LinearLayout) findViewById(R.id.progress_bar);
        Intent intent=getIntent();
        final ImageView imageView=(ImageView) findViewById(R.id.photo);

        String url=intent.getStringExtra("url");
        Picasso.with(this).load(url).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                l1.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                System.out.println("loaded Image");
            }

            @Override
            public void onError() {
                System.out.println("Unable to load Image");
            }
        });
        /*PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imageView);
        pAttacher.update();*/
    }
}
