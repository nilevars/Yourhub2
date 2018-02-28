package com.example.ae.yourhub;

import android.content.Context;
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
 * Created by A E on 17-Jul-17.
 */

public class DirectoryDatabase {
    String title,link,image,contact,timestamp,location,latitude,longitude;
    String type;
    private static final String REGISTER_URL = "http://q8hub.com/yourhub/directory/directory_insert.php";
    public static final String KEY_USER = "user_id";
    public static final String KEY_COMM = "comm_id";
    Context context;
    Session session;
    DirectoryDatabase(Context c, String title, String link,String contact,String type,String location,String latitude,String longitude, String image)
    {
        context=c;
        this.title=title;
        this.link=link;
        this.type=type;
        this.location=location;
        this.latitude=latitude;
        this.longitude=longitude;
        this.image=image;
        Long tsLong = System.currentTimeMillis() / 1000;
        timestamp = tsLong.toString();
        this.contact=contact;
        session=new Session(context);
    }
    boolean insert_data()
    {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.i("Response",response);
                        Toast.makeText(context,"Inserted",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                String user_id=hub.getString("user_id","");
                String comm_id=hub.getString("comm_id","");
                Map<String,String> params = new HashMap<String, String>();
                params.put("title",title);
                params.put("link", link);
                params.put("location", location);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("contact", contact);
                params.put("type", type);
                params.put("image", image);
                if(!(image.matches("")))
                {
                    params.put("filename", "IMG_"+timestamp);
                }
                params.put(KEY_USER, user_id);
                params.put(KEY_COMM, comm_id);
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
