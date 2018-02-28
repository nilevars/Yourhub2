package com.example.ae.yourhub;

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

/**
 * Created by A E on 02-Jul-17.
 */

public class EventDatabase {
    String n, d,loc,dt,st,et,image,timestamp;
    int type;
    private static final String REGISTER_URL = "http://q8hub.com/yourhub/events/insert_to_events.php";
    Context context;
    Session session;
    EventDatabase(Context c, String n, String d,String dt,String loc,String st,String et,String image)
    {
        context=c;
        this.n=n;
        this.d=d;
        this.dt=dt;
        this.st=st;
        this.et=et;
        this.image=image;
        this.loc=loc;
        Long tsLong = System.currentTimeMillis() / 1000;
        timestamp = tsLong.toString();

        session=new Session(context);
    }
    boolean insert_data()
    {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response",response);
                        Toast.makeText(context,"Inserted",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(context,HomeActivity.class);
                        context.startActivity(intent);
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
                params.put("name",n);
                params.put("desc",d);
                params.put("date",dt);
                params.put("starts",st);
                params.put("ends",et);
                params.put("loc",loc);
                params.put("image", image);
                if(!(image.matches("")))
                {
                    params.put("filename", "IMG_"+timestamp);
                }
                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                String user_id=hub.getString("user_id","");
                String comm_id=hub.getString("comm_id","");
                params.put("user_id", user_id);
                params.put("comm_id", comm_id);
                System.out.println("params:"+params.toString());
                Log.e("Insert params", params.toString());
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
