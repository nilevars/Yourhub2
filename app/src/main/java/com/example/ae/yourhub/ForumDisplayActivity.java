package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForumDisplayActivity extends AppCompatActivity {

    String id,t,d;
    View inflated;
    ListView listView;
    CommentList commentList;
    List<UserComments> userCommentsList=new ArrayList<UserComments>();
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_display);
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

        Intent intent=getIntent();
        t=intent.getStringExtra("title");
        d=intent.getStringExtra("desc");
        id=intent.getStringExtra("id");
        int type=Integer.parseInt(intent.getStringExtra("type"));

        listView=(ListView) findViewById(R.id.comment_list);
        commentList=new CommentList(this,userCommentsList);


        mRequestQueue=getRequestQueue();

        dataFetch();


        listView.setAdapter(commentList);
        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);



        if(type==1)
        {
            stub.setLayoutResource(R.layout.article_card);
            inflated = stub.inflate();
        }
        if (type==2)
        {
            String url=intent.getStringExtra("url");
            stub.setLayoutResource(R.layout.photo_card);
            inflated = stub.inflate();
            ImageView imageView=(ImageView) inflated.findViewById(R.id.thumbnail);
            Picasso.with(this).load(url).resize(400, 400).centerCrop().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    System.out.println("loaded Image");
                }

                @Override
                public void onError() {
                    System.out.println("Unable to load Image");
                }
            });
        }


        TextView textViewTitle=(TextView) inflated.findViewById(R.id.title);
        TextView textViewDesc=(TextView) inflated.findViewById(R.id.description);
        TextView textViewCommc=(TextView) inflated.findViewById(R.id.community);
        textViewDesc.setMaxLines(Integer.MAX_VALUE);
        textViewTitle.setText(t);
        textViewDesc.setText(d);
        SharedPreferences hub=this.getSharedPreferences("com.example.ae.yourhub", Context.MODE_PRIVATE);
        textViewCommc.setText(hub.getString("community",""));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForumDisplayActivity.this,AnswerActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("que",t);
                intent.putExtra("desc",d);
                startActivity(intent);
            }
        });
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(this.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
    void dataFetch()
    {
        String JsonURL="http://q8hub.com/yourhub/forum/get_comments.php?id="+id;
        Log.e("url",JsonURL);
        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                JsonURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jo) {
                Log.d("Response", jo.toString());
                try {
                    int s = jo.getInt("success");
                    if (s == 1) {
                        JSONArray users = jo.getJSONArray("comments");
                        for (int i = 0; i < users.length(); i++)
                        {
                            JSONObject o = users.getJSONObject(i);
                            String id = o.getString("id");
                            Log.d("id", id);
                            System.out.print("id" + id);
                            String name = StringEscapeUtils.unescapeHtml3(o.getString("name"));
                            System.out.print("id" + id);
                            String desc= StringEscapeUtils.unescapeHtml3(o.getString("desc"));
                            Log.d("desc", desc);
                            String time=o.getString("time");
                            UserComments uc=new UserComments(id,name,desc,time);
                            userCommentsList.add(uc);

                        }
                        commentList.notifyDataSetChanged();
                    }
                } catch (JSONException e) {}
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(req);
    }

}
