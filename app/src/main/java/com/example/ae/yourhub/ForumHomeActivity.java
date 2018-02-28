package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForumHomeActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager mLayoutManager;
    Context context;
    ForumAdapter forumAdapter;
    private List<ForumData> forumDatas=new ArrayList<ForumData>();
    private RequestQueue mRequestQueue;
    LinearLayout progress,network;
    Session session;
    boolean _areLecturesLoaded = false;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int s=0,n=5;
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classified_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Forum");
        TextView title= (TextView) findViewById(R.id.title);
        title.setText("Forum");

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_clear);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        context = this;
        session=new Session(context);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        forumAdapter=new ForumAdapter(this,forumDatas);
        mRequestQueue = getRequestQueue();
        recyclerView.setAdapter(forumAdapter);
        progress=(LinearLayout) findViewById(R.id.progress_bar);
        network = (LinearLayout) findViewById(R.id.network);
        if(AppStatus.getInstance(context).isOnline())
        {
            dataFetch4();
        }
        else
        {
            progress.setVisibility(View.GONE);
            network.setVisibility(View.VISIBLE);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForumHomeActivity.this,PostImageActivity.class);
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
    void dataFetch4()
    {
        SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub", Context.MODE_PRIVATE);
        String comm_id=hub.getString("comm_id","");
        String JsonURL="http://q8hub.com/yourhub/forum/get_all.php?comm_id="+comm_id;
        Log.e("url",JsonURL);
        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                JsonURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jo) {
                Log.e("Article Response", jo.toString());
                try {
                    int success = jo.getInt("success");
                    if (success == 1) {
                        JSONArray users = jo.getJSONArray("post");
                        totalItemCount=users.length();
                        if(users.length()<n)
                        {
                            n=users.length();
                        }
                        for (int i = s; i < users.length(); i++)
                        {
                            JSONObject o = users.getJSONObject(i);
                            String id = o.getString("id");
                            Log.d("id", id);
                            System.out.print("id" + id);
                            int type=Integer.parseInt(o.getString("type"));
                            int like=Integer.parseInt(o.getString("like"));
                            String title = StringEscapeUtils.unescapeHtml3(o.getString("title"));
                            System.out.print("id" + id);
                            String desc= StringEscapeUtils.unescapeHtml3(o.getString("desc"));
                            Log.d("desc", desc);
                            String time=o.getString("time");
                            String img="";
                            img = StringEscapeUtils.unescapeHtml3(o.getString("image"));
                            Log.d("image",img);
                            ForumData f=new ForumData(id,title,desc,img,2,time,like);
                            if(!(containsId(id)))
                            {
                                forumDatas.add(f);
                            }
                        }
                        s=n;
                        n+=5;
                        forumAdapter.notifyDataSetChanged();
                        progress.setVisibility(View.GONE);
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
    public boolean containsId(String id) {
        for(ForumData f:forumDatas)
        {
            if(f.getId().equals(id))
            {
                return true;
            }
        }
        return false;
    }

}
