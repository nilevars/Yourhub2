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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A E on 21-Jun-17.
 */

public class ClassifiedDatabase{
    String title,desc,image,price,timestamp,location,latitude,longitude;
    int type;
    private static final String REGISTER_URL = "http://q8hub.com/yourhub/classifieds/classified_insert.php";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "desc";
    public static final String KEY_LOC = "location";
    public static final String KEY_LAT = "latitude";
    public static final String KEY_LNG = "longitude";
    public static final String KEY_PRICE = "price";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user_id";
    public static final String KEY_COMM = "comm_id";
    Context context;
    Session session;
    ClassifiedDatabase(Context c, String title, String desc,String price,String location,String latitude,String longitude, String image)
    {
        context=c;
        this.title=title;
        this.desc=desc;
        System.out.print("title:"+title);
        Log.d("title",title);
        Log.d("type",""+type);
        this.location=location;
        this.latitude=latitude;
        this.longitude=longitude;
        this.image=image;
        Long tsLong = System.currentTimeMillis() / 1000;
        timestamp = tsLong.toString();
        this.price=price;
        session=new Session(context);
    }
    boolean insert_data()
    {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int text=Integer.parseInt(jsonObject.getString("text"));
                            int image=Integer.parseInt(jsonObject.getString("image"));
                            if(text==1&&image==1)
                            {
                                Toast.makeText(context,"Your Ad has been posted",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                String user_id=hub.getString("user_id","");
                String comm_id=hub.getString("comm_id","");
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TITLE,title);
                params.put(KEY_DESC, desc);
                params.put(KEY_LOC, location);
                params.put(KEY_LAT, latitude);
                params.put(KEY_LNG, longitude);
                params.put(KEY_PRICE, price);
                params.put(KEY_IMAGE, image);
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
