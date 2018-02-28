package com.example.ae.yourhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateCommunityActivity extends AppCompatActivity {
    EditText cname;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);
        submit=(Button) findViewById(R.id.submit);
        cname=(EditText) findViewById(R.id.cname);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=cname.getText().toString();
                if(name.matches(""))
                {
                    Toast.makeText(CreateCommunityActivity.this,"Please Enter Community Name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    CommunityVolley communityVolley=new CommunityVolley(CreateCommunityActivity.this,name);
                    if(communityVolley.insert())
                    {
                        Intent intent=new Intent(CreateCommunityActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });
        Button join=(Button) findViewById(R.id.button2);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreateCommunityActivity.this,SelectCommunityActivity.class);
                startActivity(intent);
            }
        });
    }
}
