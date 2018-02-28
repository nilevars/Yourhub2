package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    String id;
    ListView listView;
    Button post;
    TextView comment_data;
    CommentList commentList;
    Session session;
    List<UserComments> userCommentsList=new ArrayList<UserComments>();
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ActionBar ab=getSupportActionBar();
        session=new Session(CommentActivity.this);
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.comment);
        mRequestQueue=getRequestQueue();
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        listView=(ListView) findViewById(R.id.comment_list);
        commentList=new CommentList(this);
        dataFetch();
        listView.setAdapter(commentList);
        post=(Button) findViewById(R.id.post_button);
        comment_data=(TextView) findViewById(R.id.comment_data);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=comment_data.getText().toString();
                if(text.length()>0)
                {
                    insert(text);
                }
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
                            UserComments uc=new UserComments(id,name,desc,"");
                            userCommentsList.add(uc);
                        }
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }
    void insert(final String text)
    {
         final String REGISTER_URL = "http://q8hub.com/android_connect/forum/insert_comments.php";
         final String KEY_COMMENT = "comment";
         final String KEY_POST = "post_id";
         final String KEY_ID= "user_id";
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("Comment response",response);
                            int s = jsonObj.getInt("success");
                            if(s==1)
                            {
                                Session session=new Session(CommentActivity.this);
                                UserComments uc=new UserComments(id,session.getusename(),text,"");
                                userCommentsList.add(uc);
                                comment_data.setText(" ");
                                commentList.notifyDataSetChanged();
                            }
                        }
                        catch (JSONException je){}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_COMMENT,text);
                params.put(KEY_POST, id);
                params.put(KEY_ID,session.getid());
                System.out.println("params:"+params.toString());
                Log.e("comment params",params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    class CommentList extends BaseAdapter
    {
        private LayoutInflater inflater=null;
        Context context;
        CommentList(Context context)
        {
            this.context=context;
            inflater = ( LayoutInflater ) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return userCommentsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Holder holder=new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.list_comment, null);

            holder.name=(TextView) rowView.findViewById(R.id.name);
            holder.desc=(TextView) rowView.findViewById(R.id.comment);

            String uname=userCommentsList.get(i).getName();
            String desc=userCommentsList.get(i).getDescription();

            holder.name.setText(uname);
            holder.desc.setText(desc);
            return rowView;
        }
    }
    class Holder
    {
        TextView name;
        TextView desc;
    }
}
