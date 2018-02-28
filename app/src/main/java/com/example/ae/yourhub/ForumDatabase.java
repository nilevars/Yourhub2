package com.example.ae.yourhub;

/**
 * Created by A E on 3/9/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ForumDatabase {
    String title,desc,image,timestamp;
    int type;
    private static final String REGISTER_URL = "http://q8hub.com/yourhub/forum/forum_insert.php";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "desc";
    public static final String KEY_TYPE = "type";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user_id";
    Context context;
    Session session;
    ForumDatabase(Context c, String title, String desc, String image)
    {
        context=c;
        this.title=title;
        if(desc.matches(""))
        {
            this.desc=" ";
        }
        else
        {
            this.desc=desc;
        }
        System.out.print("title:"+title);
        Log.d("title",title);
        this.image=image;
        Long tsLong = System.currentTimeMillis() / 1000;
        timestamp = tsLong.toString();
        session=new Session(context);
    }
    boolean insert()
    {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context,"Inserted",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TITLE,title);
                params.put(KEY_DESC, desc);
                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                String user_id=hub.getString("user_id","");
                String comm_id=hub.getString("comm_id","");
                params.put(KEY_USER, session.getid());
                System.out.println("params:"+params.toString());
                Log.e("Insert params", params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return true;
    }
    boolean insert_image()
    {
        Log.i("url",REGISTER_URL);
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response",response);
                        //Toast.makeText(this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Response",error.toString());
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                String user_id=hub.getString("userid","");
                String comm_id=hub.getString("comm_id","");
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TITLE,title);
                params.put(KEY_DESC, ""+desc);
                params.put(KEY_TYPE, ""+type);
                params.put(KEY_IMAGE, image);
                params.put("filename", "IMG_"+timestamp);
                params.put("userid", user_id);
                params.put("comm_id", comm_id);
                Log.i("Insert params", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        return true;
    }
}
