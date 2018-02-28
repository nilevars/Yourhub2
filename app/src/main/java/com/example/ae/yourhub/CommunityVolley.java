package com.example.ae.yourhub;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A E on 29-May-17.
 */

public class CommunityVolley {
    String cname,desc,image,timestamp;
    int type;
    private static final String REGISTER_URL = "http://q8hub.com/yourhub/community_info/create_community.php";
    public static final String KEY_NAME = "cname";
    public static final String KEY_USER = "user_id";
    Context context;
    Session session;
    CommunityVolley(Context c, String cname)
    {
        context=c;
        this.cname=cname;
        System.out.print("title:"+cname);
        Log.d("title",cname);
        session=new Session(context);
    }
    boolean insert()
    {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("response",response);
                            int s = jsonObj.getInt("success");
                            if(s==1)
                            {
                                String name=jsonObj.getString("name");
                                String community_id=jsonObj.getString("community_id");
                                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                                hub.edit().putString("comm_id",community_id).commit();
                                hub.edit().putString("community",name).commit();
                                session.setcommid(community_id);
                                session.setcommunity(name);
                            }

                        }
                        catch(Exception e){
                            Log.e("User Response",e.toString());
                        }
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
                params.put(KEY_NAME,cname);
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
}
