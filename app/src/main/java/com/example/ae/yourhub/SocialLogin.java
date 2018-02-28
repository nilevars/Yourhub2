package com.example.ae.yourhub;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A E on 3/7/2017.
 */

public class SocialLogin {
    String fname,email,type,id;
    private static final String REGISTER_URL = "http://q8hub.com/yourhub/forum/login_social.php";
    public static final String KEY_USERNAME = "fname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TYPE = "type";
    public static final String KEY_ID= "id";
    Context context;
    Session session;
    SocialLogin(Context c, String fname, String email, String id, String type)
    {

        context=c;
        session=new Session(c);
        this.id=id;
        this.fname=fname;
        System.out.println("fname:"+fname);
        Log.d("fname:",fname);
        this.email=email;
        this.type=type;
    }
    void insert()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObj = new JSONObject(response);
                            Log.i("response",response);
                            int s = jsonObj.getInt("success");
                            String userid=jsonObj.getString("user_id");
                            String name=jsonObj.getString("name");
                            if(s==1)
                            {
                                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                                hub.edit().putString("username",name).commit();
                                hub.edit().putString("userid",userid).commit();
                                session.setusename(name);
                                session.setid(userid);
                            }
                        }
                        catch(Exception e){
                            Log.i("User Response",e.toString());
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
                params.put(KEY_USERNAME,fname);
                params.put(KEY_EMAIL, email);
                params.put(KEY_TYPE, type);
                params.put(KEY_ID,id);
                Log.e("params",params.toString());
                System.out.println("params:"+params.toString());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
