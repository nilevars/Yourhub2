package com.example.ae.yourhub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by A E on 20-Jul-17.
 */

public class DirectoryHomePage extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;

    Context context;
    private List<DirectoryData> directoryDataList = new ArrayList<DirectoryData>();
    DirectoryAdapter directoryAdapter;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        session = new Session(context);
        View view = inflater.inflate(R.layout.photo_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        directoryAdapter=new DirectoryAdapter(context,directoryDataList);

        gm = new GridLayoutManager(context, 2);

        recyclerView.setLayoutManager(gm);
        recyclerView.setAdapter(directoryAdapter);

        mRequestQueue = getRequestQueue();
        progress = (LinearLayout) view.findViewById(R.id.progress_bar);

        network = (LinearLayout) view.findViewById(R.id.network);
        if(AppStatus.getInstance(context).isOnline())
        {
            dataFetch();
        }
        else
        {
            progress.setVisibility(View.GONE);
            network.setVisibility(View.VISIBLE);
        }

        progress.setVisibility(View.GONE);
        return view;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    void dataFetch() {
        SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
        String comm_id=hub.getString("comm_id","");
        String JsonURL = "http://q8hub.com/yourhub/directory/get_places.php?comm_id="+comm_id;
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
                        JSONArray users = jo.getJSONArray("places");
                        Log.i("json array",users.toString());
                        totalItemCount=users.length();
                        Log.i("length",""+users.length());
                        if(users.length()<n)
                        {
                            n=users.length();
                        }
                        for (int i = 0; i < users.length(); i++){
                            Log.i("i",""+i);
                            JSONObject o = users.getJSONObject(i);
                            Log.i("obj",o.toString());
                            String t = o.getString("place_name");
                            Log.i("title",t);
                            String d = o.getString("place_link");
                            String p = o.getString("place_type");
                            String loc = o.getString("location_name");
                            String lat = o.getString("place_latitude");
                            String lng = o.getString("place_longitude");
                            String img = o.getString("logo_link");
                            Log.i("img",img);
                            String id = o.getString("id_place");
                            DirectoryData f = new DirectoryData(id, t, d,p,loc,lat,lng,img);
                            if (!(containsId(id))) {
                                directoryDataList.add(f);
                                Log.e("added","yes");
                            }
                        }
                        directoryAdapter.notifyDataSetChanged();
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
    @Override
    public void onDestroy() {
        super.onDestroy();
       /* HomePage2 myf=new HomePage2();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.add(R.id.content, myf);
        transaction.commit();*/

    }
    public boolean containsId(String id){
        for(DirectoryData f:directoryDataList)
        {
            if(f.getId().equals(id))
            {
                return true;
            }
        }
        return false;
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Directory");
    }
}
