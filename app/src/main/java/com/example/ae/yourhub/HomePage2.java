package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * Created by A E on 04-Jul-17.
 */

public class HomePage2 extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager,mLayoutManager2;

    Context context;
    private List<NewsData> newsDataList = new ArrayList<NewsData>();
    NewsAdapter newsAdapter;

    private List<ClassifiedData> classifiedDatas = new ArrayList<ClassifiedData>();
    ClassifiedsAdapter classifiedsAdapter;

    private List<EventData> eventDataList = new ArrayList<EventData>();
    EventRecyclerAdapter eventRecyclerAdapter;


    ForumAdapter forumAdapter;
    private List<ForumData> forumDatas=new ArrayList<ForumData>();

    LinearLayout main,rect;

    private RequestQueue mRequestQueue;
    private StaggeredGridLayoutManager gm;
    LinearLayout progress;
    Session session;
    RecyclerView.Adapter adapter;
    FloatingActionButton fab;
    ImageView classified,forum,event,news,place;
    Boolean loaded=false,isVisibleToUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        session = new Session(context);
        View view = inflater.inflate(R.layout.home_page2, container, false);
        fab=(FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        classified=(ImageView) view.findViewById(R.id.classified_icon);
        classified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassifiedHomePage myf=new ClassifiedHomePage();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.addToBackStack(null);
                transaction.add(R.id.content, myf);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setBackgroundTintList(getResources().getColorStateList(R.color.green_500));
                fab.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),ClassifiedsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        forum=(ImageView) view.findViewById(R.id.forum_icon);
        forum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ForumHomePage myf = new ForumHomePage();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.addToBackStack(null);
                transaction.add(R.id.content, myf);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setBackgroundTintList(getResources().getColorStateList(R.color.yellow_800));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),PostImageActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });
        event=(ImageView) view.findViewById(R.id.event_icon);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventHomePage2 myf = new EventHomePage2();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.addToBackStack(null);
                transaction.add(R.id.content, myf);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setBackgroundTintList(getResources().getColorStateList(R.color.blue_900));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),PostEventsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        news=(ImageView) view.findViewById(R.id.news_icon);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NewsHomePage myf=new NewsHomePage();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.addToBackStack(null);
                transaction.add(R.id.content, myf);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setBackgroundTintList(getResources().getColorStateList(R.color.pink_400));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),NewsPostActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        place=(ImageView) view.findViewById(R.id.place_icon);
        place.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DirectoryHomePage myf=new DirectoryHomePage();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.addToBackStack(null);
                transaction.add(R.id.content, myf);
                transaction.commit();

                fab.setVisibility(View.VISIBLE);
                fab.setBackgroundTintList(getResources().getColorStateList(R.color.cyan_700));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),DirectoryActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        mRequestQueue = getRequestQueue();
        main=(LinearLayout) view.findViewById(R.id.main_content);
        rect=(LinearLayout) view.findViewById(R.id.rect);

        //Classifieds Recycler
        if(isNetworkAvailable())
        {
            classifiedsAdapter=new ClassifiedsAdapter(context,classifiedDatas);
            addData(classifiedsAdapter,"Classifieds",1);
            Log.e("Size of classifieds",""+classifiedDatas.size());
            newsAdapter = new NewsAdapter(context, newsDataList);
            addData(newsAdapter,"News",3);
            Log.e("Size of News",""+newsDataList.size());
            forumAdapter=new ForumAdapter(getActivity(),forumDatas);
            addData(forumAdapter,"Forum",4);
            Log.e("Size of Forum",""+forumDatas.size());
            eventRecyclerAdapter=new EventRecyclerAdapter(context,eventDataList);
            Log.e("Size of Events",""+eventDataList.size());
            addData(eventRecyclerAdapter,"Events",2);
            if(loaded)
            {
                rect.setVisibility(View.GONE);
                main.setVisibility(View.VISIBLE);
            }

        }

        else
        {
            LinearLayout network=(LinearLayout) view.findViewById(R.id.network);
            network.setVisibility(View.VISIBLE);
            rect.setVisibility(View.GONE);
            Button retry=(Button) view.findViewById(R.id.btnRetry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,HomeActivity.class);
                    startActivity(intent);
                }
            });
        }
        return view;
    }

    void addData(RecyclerView.Adapter adapter, String title,int i)
    {

        this.adapter=adapter;
        LinearLayout l=new LinearLayout(getActivity());
        l.setOrientation(LinearLayout.VERTICAL);

        Log.e("Adapter",""+i);
        RecyclerView recyclerView=new RecyclerView(getActivity());
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);

        recyclerView.setAdapter(adapter);

        TextView textView=new TextView(getActivity());
        textView.setText(title);

        l.setLayoutParams(layoutParams);

        dataFetch(i);
        l.addView(textView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        if(i==3)
        {
           // l.setBackgroundColor(getResources().getColor(R.color.grey_100));

        }
        if(i==2)
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        }
        l.addView(recyclerView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        main.addView(l);

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void dataFetch(int i){
        SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
        String comm_id=hub.getString("comm_id","");
        if(i==1)
        {
            String JsonURL = "http://q8hub.com/yourhub/classifieds/get_ads.php?comm_id="+comm_id;
            Log.e("url", JsonURL);
            JsonObjectRequest req1 = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                    JsonURL, null, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject jo){
                    Log.e("Response", jo.toString());
                    try {
                        int success = jo.getInt("success");
                        Log.i("success:", Integer.toString(success));
                        if (success == 1){
                            JSONArray users = jo.getJSONArray("ads");
                            Log.i("json array",users.toString());
                            Log.i("length",""+users.length());
                            int n=5;
                            if(users.length()<n)
                            {
                                n=users.length();
                            }
                            for (int i = 0; i < n; i++){
                                Log.i("i",""+i);
                                JSONObject o = users.getJSONObject(i);
                                Log.i("obj",o.toString());
                                String t = o.getString("title");
                                Log.i("title",t);
                                String d = o.getString("desc");
                                String p = o.getString("price");
                                String loc = o.getString("location");
                                String lat = o.getString("latitude");
                                String lng = o.getString("longitude");
                                String img = o.getString("image");
                                Log.i("img",img);
                                String id = o.getString("id");
                                ClassifiedData f = new ClassifiedData(id, t, d,p,loc,lat,lng,img);
                                if (!(containsCid(id))) {
                                    classifiedDatas.add(f);
                                }
                            }
                            Log.e("Size of classifieds",""+classifiedDatas.size());
                            classifiedsAdapter.notifyDataSetChanged();
                            rect.setVisibility(View.GONE);
                            main.setVisibility(View.VISIBLE);
                            Log.e("Classifieds Adapter","Notified");

                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {}
            });
            mRequestQueue.add(req1);
        }
        else if(i==2)
        {
            String JsonURL = "http://q8hub.com/yourhub/events/get_event.php?comm_id="+comm_id;
            Log.e("url", JsonURL);

            JsonObjectRequest req2 = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                    JsonURL, null, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject jo) {
                    Log.e("Response", jo.toString());
                    try {
                        int success = jo.getInt("success");
                        Log.i("success:", Integer.toString(success));
                        if (success == 1){
                            JSONArray users = jo.getJSONArray("events");

                            int n=4;
                            if (users.length() < n){
                                n = users.length();
                            }

                            for (int i = 0; i < n; i++)
                            {
                                JSONObject o = users.getJSONObject(i);
                                String id = o.getString("id");
                                String name = StringEscapeUtils.unescapeHtml3(o.getString("name"));
                                String img = StringEscapeUtils.unescapeHtml3(o.getString("image"));
                                String date=StringEscapeUtils.unescapeHtml3(o.getString("date"));
                                String time=o.getString("starts")+"-"+o.getString("ends");
                                String location=o.getString("location");
                                EventData eventData=new EventData(id,name,img,date,time,location);
                                if (!(containsEid(id))) {
                                    eventDataList.add(eventData);
                                }
                                Log.e("status","added");
                            }
                            Log.e("Size of Events",""+eventDataList.size());
                            eventRecyclerAdapter.notifyDataSetChanged();
                            Log.e("Event Adapter","Notified");
                            rect.setVisibility(View.GONE);
                            main.setVisibility(View.VISIBLE);
                            loaded=true;
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
            mRequestQueue.add(req2);
        }
        else if(i==3)
        {
            String JsonURL = "http://q8hub.com/yourhub/news/get_news2.php?comm_id="+comm_id;
            Log.e("url", JsonURL);
            JsonObjectRequest req3 = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                    JsonURL, null, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject jo) {
                    Log.e("Response", jo.toString());
                    try {
                        int success = jo.getInt("success");
                        Log.i("success:", Integer.toString(success));
                        if (success == 1){
                            JSONArray users = jo.getJSONArray("news");
                            Log.i("json array", users.toString());
                            Log.i("length", "" + users.length());
                            int n=5;
                            if (users.length() < n){
                                n = users.length();
                            }
                            for (int i = 0; i < n; i++){
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
                                if (!(containsNid(id))) {
                                    newsDataList.add(newsData);
                                }
                                Log.i("h", "helo");
                            }
                            Log.e("Size of News",""+newsDataList.size());
                            newsAdapter.notifyDataSetChanged();
                            rect.setVisibility(View.GONE);
                            main.setVisibility(View.VISIBLE);
                            Log.e("News Adapter","Notified");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {}
            });
            mRequestQueue.add(req3);
        }
        else if(i==4)
        {
            String JsonURL="http://q8hub.com/yourhub/forum/get_all.php?comm_id="+comm_id;
            Log.e("url",JsonURL);
            JsonObjectRequest req4 = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                    JsonURL, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jo) {
                    Log.e("Article Response", jo.toString());
                    try {
                        int success = jo.getInt("success");
                        if (success == 1) {
                            JSONArray users = jo.getJSONArray("post");
                            int n=5;
                            if(users.length()<n)
                            {
                                n=users.length();
                            }
                            for (int i = 0; i < n; i++)
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
                                if (!(containsFid(id))) {
                                    forumDatas.add(f);
                                }
                            }
                            Log.e("Size of Forum",""+forumDatas.size());
                            forumAdapter.notifyDataSetChanged();
                            rect.setVisibility(View.GONE);
                            main.setVisibility(View.VISIBLE);
                            Log.e("Forum Adapter","Notified");

                        }
                    } catch (JSONException e){}
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {}
            });
            mRequestQueue.add(req4);
        }

    }
    public boolean containsCid(String id){
        for(ClassifiedData f:classifiedDatas)
        {
            if(f.getId().equals(id))
            {
                return true;
            }
        }
        return false;
    }
    public boolean containsFid(String id) {
        for(ForumData f:forumDatas)
        {
            if(f.getId().equals(id))
            {
                return true;
            }
        }
        return false;
    }
    public boolean containsEid(String id){
        for(EventData f:eventDataList)
        {
            if(f.getId().equals(id))
            {
                return true;
            }
        }
        return false;
    }
    public boolean containsNid(String id) {
        for (NewsData f : newsDataList) {
            if (f.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle("Home");
        fab.setVisibility(View.GONE);
    }
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            getActivity().setTitle("Home");
        }
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
