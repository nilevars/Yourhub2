package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsHomeActivity extends AppCompatActivity {

    Context context;
    private List<NewsData> newsDataList = new ArrayList<NewsData>();
    NewsAdapter newsAdapter;
    private RequestQueue mRequestQueue;
    private GridLayoutManager gm;
    LinearLayout progress,network;
    Session session;
    boolean _areLecturesLoaded = false;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int s = 0, n = 5;
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classified_home);

        context=this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title= (TextView) findViewById(R.id.title);
        title.setText("News");

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_clear);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        newsAdapter = new NewsAdapter(context, newsDataList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewsHomeActivity.this,NewsPostActivity.class);
                startActivity(intent);
            }
        });

        gm = new GridLayoutManager(context, 2);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(newsAdapter);

        mRequestQueue = getRequestQueue();
        progress = (LinearLayout) findViewById(R.id.progress_bar);
        network = (LinearLayout) findViewById(R.id.network);
        if(AppStatus.getInstance(context).isOnline())
        {
            dataFetch3();
        }
        else
        {
            progress.setVisibility(View.GONE);
            network.setVisibility(View.VISIBLE);
        }
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

    void dataFetch3() {

        SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub", Context.MODE_PRIVATE);
        String comm_id=hub.getString("comm_id","");
        String JsonURL = "http://q8hub.com/yourhub/news/get_news2.php?comm_id="+comm_id;
        Log.e("url", JsonURL);
        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                JsonURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jo) {
                Log.i("Response", jo.toString());
                try {
                    int success = jo.getInt("success");
                    Log.i("success:", Integer.toString(success));
                    if (success == 1) {
                        JSONArray users = jo.getJSONArray("news");
                        Log.i("json array", users.toString());
                        totalItemCount = users.length();
                        Log.i("length", "" + users.length());
                        if (users.length() < n) {
                            n = users.length();
                        }
                        for (int i = s; i < users.length(); i++) {
                            Log.i("i", "" + i);
                            JSONObject o = users.getJSONObject(i);
                            Log.i("obj", o.toString());
                            String tn = o.getString("title_news");
                            String t = o.getString("title");
                            String link = o.getString("link");
                            String desc = o.getString("description");
                            String image = o.getString("image");
                            Log.i("title", t);
                            String d = o.getString("link");
                            String id = o.getString("id");
                            NewsData newsData=new NewsData(id,tn,t,link,desc,image);

                            if (!(containsId(id))) {

                                newsDataList.add(newsData);
                                Log.i("h", "helo");

                            }
                        }
                        s = n;
                        n += 5;
                        newsAdapter.notifyDataSetChanged();
                        progress.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(req);
    }

    public boolean containsId(String id) {
        for (NewsData f : newsDataList) {
            if (f.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

}
