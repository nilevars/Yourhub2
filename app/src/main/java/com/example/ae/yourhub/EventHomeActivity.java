package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventHomeActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager mLayoutManager;

    Context context;
    private List<ClassifiedData> classifiedDatas = new ArrayList<ClassifiedData>();
    ClassifiedsAdapter classifiedsAdapter;
    EventRecyclerAdapter eventRecyclerAdapter;
    private RequestQueue mRequestQueue;
    private StaggeredGridLayoutManager gm;
    private List<EventData> eventDataList = new ArrayList<EventData>();
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
        title.setText("Event");

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_clear);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EventHomeActivity.this,EventsActivity.class);
                startActivity(intent);
            }
        });


        context = this;
        session=new Session(context);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        eventRecyclerAdapter=new EventRecyclerAdapter(context,eventDataList);

        gm = new StaggeredGridLayoutManager(2, 1);

        recyclerView.setLayoutManager(gm);
        recyclerView.setAdapter(eventRecyclerAdapter);

        mRequestQueue = getRequestQueue();

        progress = (LinearLayout) findViewById(R.id.progress_bar);
        progress.setVisibility(View.GONE);

        network = (LinearLayout) findViewById(R.id.network);
        if(AppStatus.getInstance(context).isOnline())
        {
            dataFetch2();
        }
        else
        {
            progress.setVisibility(View.GONE);
            network.setVisibility(View.VISIBLE);
        }


    }
    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            Cache cache = new DiskBasedCache(this.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    void dataFetch2(){

        SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
        String comm_id=hub.getString("comm_id","");
        String JsonURL = "http://q8hub.com/yourhub/events/get_event.php?comm_id="+comm_id;
        Log.e("url", JsonURL);

        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                JsonURL, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject jo) {
                Log.i("Response", jo.toString());
                try {
                    int success = jo.getInt("success");
                    Log.i("success:", Integer.toString(success));
                    if (success == 1) {
                        JSONArray users = jo.getJSONArray("events");
                        for (int i = 0; i < users.length(); i++)
                        {
                            JSONObject o = users.getJSONObject(i);
                            String id = o.getString("id");
                            String name = StringEscapeUtils.unescapeHtml3(o.getString("name"));
                            String img = StringEscapeUtils.unescapeHtml3(o.getString("image"));
                            String date=StringEscapeUtils.unescapeHtml3(o.getString("date"));
                            String time=o.getString("starts")+"-"+o.getString("ends");
                            String location=o.getString("location");
                            EventData eventData=new EventData(id,name,img,date,time,location);
                            if (!(containsId(id))){
                                eventDataList.add(eventData);
                                Log.e("status","added");

                            }
                            else
                            {
                                Log.e("status","not added");
                            }
                        }
                        eventRecyclerAdapter.notifyDataSetChanged();
                        progress.setVisibility(View.GONE);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(req);
    }
    public boolean containsId(String id){
        for(EventData f:eventDataList)
        {
            if(f.getId().equals(id))
            {
                return true;
            }
        }
        return false;
    }
}
