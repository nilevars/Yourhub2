package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewsPostActivity extends AppCompatActivity {

    EditText title,link;
    private static final String REGISTER_URL = "http://q8hub.com/yourhub/news/news_insert2.php";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LINK = "link";
    public static final String KEY_USER = "user_id";
    public static final String KEY_COMM = "comm_id";
    Context context;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_clear);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        context=this;
        title=(EditText) findViewById(R.id.news_title);
        link=(EditText) findViewById(R.id.news_link);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_answer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done)
        {
            Boolean ok=true;
            String n=title.getText().toString();
            String l=link.getText().toString();
            if(n.matches(""))
            {
                ok=false;
                Toast.makeText(NewsPostActivity.this,"Enter Title",Toast.LENGTH_LONG).show();
            }
            if(l.matches(""))
            {
                ok=false;
                Toast.makeText(NewsPostActivity.this,"Enter a link",Toast.LENGTH_LONG).show();
            }

            if(ok)
            {
                insert_data();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    boolean insert_data()
    {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Status",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            int text=Integer.parseInt(jsonObject.getString("text"));
                            if(text==1)
                            {
                                Toast.makeText(context,"Your link has been posted",Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TITLE,title.getText().toString());
                params.put(KEY_LINK, link.getText().toString());
                SharedPreferences hub=context.getSharedPreferences("com.example.ae.yourhub",Context.MODE_PRIVATE);
                String user_id=hub.getString("user_id","");
                String comm_id=hub.getString("comm_id","");
                params.put(KEY_USER, user_id);
                params.put(KEY_COMM, comm_id);
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
        Intent intent=new Intent(NewsPostActivity.this,HomeActivity.class);
        startActivity(intent);
        return true;
    }
}
