package com.example.ae.yourhub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class EventPreviewActivity extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    String n,d,dt,st,et,loc,path;
    String server="http://q8hub.com/yourhub/events/insert_to_events.php",timestamp=null;
    ImageView imageView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_preview);
        context=this;
        Intent i = getIntent();
        n = i.getStringExtra("name");
        d = i.getStringExtra("desc");
        dt = i.getStringExtra("date");
        st = i.getStringExtra("st");
        et = i.getStringExtra("et");
        String time=st+"-"+et;
        loc = i.getStringExtra("loc");
        path=i.getStringExtra("img");
        LinearLayout l = (LinearLayout) findViewById(R.id.activity_event_preview);
        imageView=(ImageView) findViewById(R.id.img1);
        if(!(path.matches("")))
        {
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(this).load(new File(path)).resize(400,400).centerCrop().into(imageView, new Callback()
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
        }
        Log.i("location",loc);
        t1 = (TextView) findViewById(R.id.name);
        t1.setText(n);
        t2 = (TextView) findViewById(R.id.loc1);
        t2.setText(loc);
        t3 = (TextView) findViewById(R.id.date1);
        t3.setText(dt);
        t4 = (TextView) findViewById(R.id.time1);
        t4.setText(time);
        t5 = new TextView(this);
        t5.setText(d);
        Long tsLong = System.currentTimeMillis() / 1000;
        timestamp = tsLong.toString();
        Button btn = (Button) findViewById(R.id.btn1);
        btn.setText("Submit");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap image=((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

                EventDatabase eventDatabase=new EventDatabase(EventPreviewActivity.this,n,d,dt,loc,st,et,encodeImage);
                if(eventDatabase.insert_data())
                {
                    Intent intent=new Intent(EventPreviewActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


}


