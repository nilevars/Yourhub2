package com.example.ae.yourhub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AnswerActivity extends AppCompatActivity {
    EditText answer;
    ImageButton bold,italics;
    Context context=this;
    int start;
    Session session;
    ProgressDialog progressDialog;
    String question_id;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.write);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_clear);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        answer = (EditText) findViewById(R.id.answer);
        Intent intent = getIntent();
        question_id = intent.getStringExtra("id");
        String question=intent.getStringExtra("que");
        String desc=intent.getStringExtra("desc");


        TextView textView=(TextView) findViewById(R.id.question);
        textView.setText(question);

        TextView textView2=(TextView) findViewById(R.id.desc);
        textView2.setText(desc);


        mRequestQueue=getRequestQueue();
        session=new Session(this);
        progressDialog=new ProgressDialog(AnswerActivity.this,ProgressDialog.STYLE_SPINNER);
    }
    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(this.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
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
            progressDialog.setIndeterminate(true);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();

            String ans=answer.getText().toString();
            insert(ans);
        }
        return super.onOptionsItemSelected(item);
    }
    void insert(final String text)
    {
        final String REGISTER_URL = "http://q8hub.com/yourhub/forum/insert_comments.php";
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
                                progressDialog.dismiss();
                                finish();
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
                params.put(KEY_POST, question_id);
                params.put(KEY_ID,session.getid());
                System.out.println("params:"+params.toString());
                Log.e("comment params",params.toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
