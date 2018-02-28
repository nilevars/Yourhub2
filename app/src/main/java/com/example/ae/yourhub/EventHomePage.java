package com.example.ae.yourhub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by A E on 2/23/2017.
 */

public class EventHomePage extends Fragment {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StaggeredGridLayoutManager gm;
    ProgressBar proNews;
    private List<EventData> eventDatas = new ArrayList<EventData>();
    EventRecyclerAdapter eventRecyclerAdapter;
    int count=0;
    Context context;
    LinearLayout progress;
    private RequestQueue mRequestQueue;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getActivity();
        View view = inflater.inflate(R.layout.photo_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        eventRecyclerAdapter=new EventRecyclerAdapter(context,eventDatas);

        gm = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gm);

        recyclerView.setAdapter(eventRecyclerAdapter);
        mRequestQueue = getRequestQueue();

        progress = (LinearLayout) view.findViewById(R.id.progress_bar);
        progress.setVisibility(View.GONE);

        dataFetch();
        return view;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }
    void dataFetch(){

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
                        count=users.length();
                        for (int i = 0; i < users.length(); i++)
                        {
                            JSONObject o = users.getJSONObject(i);
                            String id = o.getString("id");
                            Log.d("id", id);
                            System.out.print("id" + id);
                            String name = StringEscapeUtils.unescapeHtml3(o.getString("name"));
                            String img = StringEscapeUtils.unescapeHtml3(o.getString("image"));
                            String date=StringEscapeUtils.unescapeHtml3(o.getString("date"));
                            String time=o.getString("starts")+"-"+o.getString("ends");
                            String location=o.getString("location");
                            EventData eventData=new EventData(id,name,img,date,time,location);
                            if (!(containsId(id))){
                                eventDatas.add(eventData);
                                Log.i("h","helo");

                            }
                        }
                        eventRecyclerAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){

            }
        });
        mRequestQueue.add(req);
    }
    public boolean containsId(String id) {
        for(EventData f:eventDatas)
        {
            if(f.getId().equals(id))
            {
                return true;
            }
        }
        return false;
    }
   /* private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        private Context context;
        String result;
        ImageView photo;
        JSONArray users = null;
        ArrayList<HashMap<String, String>> newsList;


        public GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("method", "in pre....");
            newsList = new ArrayList<HashMap<String, String>>();
            photo = new ImageView(context);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                String comm_id=hub.getString("comm_id","");
                System.out.print("in background....");
                String url_string="http://q8hub.com/yourhub/events/get_event.php?comm_id="+comm_id;
                Log.i("url",url_string);
                URL url = new URL(url_string);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
                    StringBuilder result = new StringBuilder();
                    String line;
                    urlConnection.connect();
                    System.out.print("not connected");
                    int response_code = urlConnection.getResponseCode();
                    if (response_code == HttpURLConnection.HTTP_OK) {
                        System.out.print("connected");
                        Log.d("Status", "Connected");
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                    }
                    String r = result.toString();
                    Log.d("Received:", r);
                    Log.d("Success:", r);
                    JSONObject jo = new JSONObject(r);
                    int s = jo.getInt("success");

                    if (s == 1) {
                        users = jo.getJSONArray("events");
                        count=users.length();
                        name=new String[count];
                        img=new String[count];
                        desc=new String[count];
                        date=new String[count];
                        time=new String[count];
                        location=new String[count];
                        for (int i = 0; i < users.length(); i++)
                        {
                            JSONObject o = users.getJSONObject(i);
                            String id = o.getString("id");
                            Log.d("id", id);
                            System.out.print("id" + id);
                            name[i] = StringEscapeUtils.unescapeHtml3(o.getString("name"));
                            //desc[i]= StringEscapeUtils.unescapeHtml3(o.getString("desc"));
                            img[i] = StringEscapeUtils.unescapeHtml3(o.getString("image"));
                            date[i]=StringEscapeUtils.unescapeHtml3(o.getString("date"));
                            time[i]=o.getString("starts")+"-"+o.getString("ends");
                            location[i]=o.getString("location");
                        }
                    }

                } finally {
                    urlConnection.disconnect();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.print("not connected2");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Log.d("method", "in post....");
            Log.d("method", "in post....");
           // proNews.setVisibility(View.GONE);
            mRecyclerView.setAdapter(new EventRecyclerAdapter(getActivity(),count,name,img,date,time,location));
        }

    }*/
   @Override
   public void onDestroy() {
       super.onDestroy();
       HomePage2 myf=new HomePage2();
       FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
       transaction.addToBackStack(null);
       transaction.add(R.id.content, myf);
       transaction.commit();

   }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Events");
    }
}
