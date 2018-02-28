package com.example.ae.yourhub;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A E on 15-May-17.
 */

public class LikeList {
    String userid,postid,like;
    boolean result=false;
    private static final String REGISTER_URL = "http://q8hub.com/yourhub/forum/like_insert.php";
    private static final String REGISTER_URL2 = "http://q8hub.com/yourhub/forum/like_check.php";
    public static final String KEY_POST = "post_id";
    public static final String KEY_CHOICE = "choice";
    public static final String KEY_USER = "user_id";
    Context context;
    Session session;
    LikeList(Context c, String postid)
    {
        this.postid=postid;
        context=c;
        session=new Session(context);
    }
    LikeList(Context c, String postid, String like)
    {
        this.postid=postid;
        this.like=like;
        context=c;
        session=new Session(context);
    }
    boolean insert()
    {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Log.e("insert response",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_POST,postid);
                params.put(KEY_CHOICE, like);
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
    boolean checkLike()
    {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL2,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Log.e("response",response);
                        if(response.equals("yes"))
                        {
                            result=true;
                            Log.e("result",""+result);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("r error",error.toString());
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_POST,postid);
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
    boolean testlike()
    {
        return result;
    }
}
