package com.example.ae.yourhub;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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

public class SelectCommunityActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager mLayoutManager;
    Context context;
    private List<CommunityData> communityDatas = new ArrayList<CommunityData>();
    CommunityAdapter communityAdapter;
    private RequestQueue mRequestQueue;
    private StaggeredGridLayoutManager gm;
    LinearLayout progress;
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
        setContentView(R.layout.photo_fragment);
        context=this;

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        communityAdapter = new CommunityAdapter(context, communityDatas);

        gm = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        recyclerView.setAdapter(communityAdapter);
        mRequestQueue = getRequestQueue();

        progress = (LinearLayout) findViewById(R.id.progress_bar);
        progress.setVisibility(View.GONE);
        dataFetch();
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

    void dataFetch() {
        String JsonURL = "http://q8hub.com/yourhub/community_info/get_community.php";
        Log.e("url", JsonURL);
        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                JsonURL, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject jo) {
                Log.i("Response", jo.toString());
                try {
                    int success = jo.getInt("success");
                    Log.i("success:", Integer.toString(success));
                    if (success == 1){
                        JSONArray users = jo.getJSONArray("community");
                        Log.i("json array", users.toString());
                        totalItemCount = users.length();
                        Log.i("length", "" + users.length());
                        if (users.length() < n){
                            n = users.length();
                        }
                        for (int i = 0; i < users.length(); i++) {
                            Log.i("i", "" + i);
                            JSONObject o = users.getJSONObject(i);
                            Log.i("obj", o.toString());
                            String t = o.getString("community_name");
                            Log.i("title", t);
                            String d = o.getString("user_id");
                            String id = o.getString("id_community");
                            CommunityData communityData=new CommunityData(id,t,d);

                            if (!(containsId(id))){
                                communityDatas.add(communityData);
                                Log.i("h", "helo");
                            }
                        }
                        s = n;
                        n += 5;
                        communityAdapter.notifyDataSetChanged();
                        progress.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){}
        });
        mRequestQueue.add(req);
    }

    public boolean containsId(String id) {
        for (CommunityData f : communityDatas) {
            if (f.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
