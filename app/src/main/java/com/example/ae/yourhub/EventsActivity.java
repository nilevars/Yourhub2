package com.example.ae.yourhub;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EventsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    EditText name, description, date, loc, starts, ends;
    String pathImage="";
    int PICK_IMAGE=10;
    int PLACE_PICKER_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_insert);
        mGoogleApiClient = new GoogleApiClient
                .Builder( this )
                .enableAutoManage( this, 0, this )
                .addApi( Places.GEO_DATA_API )
                .addApi( Places.PLACE_DETECTION_API )
                .addConnectionCallbacks( EventsActivity.this )
                .addOnConnectionFailedListener( this )
                .build();
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.desc_event);
        date = (EditText) findViewById(R.id.date_event);
        date.setFocusable(false);
        loc = (EditText) findViewById(R.id.locc_event);
        starts = (EditText) findViewById(R.id.start_event);
        ends = (EditText) findViewById(R.id.ends_event);
            final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    date.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
                }

            };
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog d=new DatePickerDialog(EventsActivity.this, date2, 2017,03,11);
                    d.show();

                }
            });
        starts.setFocusable(false);
        final TimePickerDialog.OnTimeSetListener timeListener=new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String m=null;
                if(minute<10)
                {
                    m="0"+minute;
                }
                else
                {
                    m=""+minute;
                }
                starts.setText(hourOfDay+":"+m);
            }

        };
        final TimePickerDialog.OnTimeSetListener timeListener2=new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String m=null;
                if(minute<10)
                {
                    m="0"+minute;
                }
                else
                {
                    m=""+minute;
                }
                ends.setText(hourOfDay+":"+m);
            }

        };
         starts.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 new TimePickerDialog(EventsActivity.this,timeListener ,0,0, DateFormat.is24HourFormat(EventsActivity.this)).show();
             }
         });
         ends.setFocusable(false);
        ends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(EventsActivity.this,timeListener2 ,0,0, DateFormat.is24HourFormat(EventsActivity.this)).show();
            }
        });
        image=(ImageView) findViewById(R.id.img_event);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
        loc.setFocusable(false);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try{
                    startActivityForResult(builder.build(EventsActivity.this), PLACE_PICKER_REQUEST);
                }
                catch ( GooglePlayServicesRepairableException e ) {
                    Log.d( "PlacesAPI Demo", "GooglePlayServicesRepairableException thrown" );}
                catch ( GooglePlayServicesNotAvailableException e ) {
                Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
            }

            }
        });
      /*  Button b=(Button) findViewById(R.id.sub_btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n=name.getText().toString();
                String d=description.getText().toString();
                String dt=date.getText().toString();
                String st=starts.getText().toString();
                String et=ends.getText().toString();
                String l=loc.getText().toString();
                Intent i=new Intent(EventsActivity.this,EventPreviewActivity.class);
                i.putExtra("name",n);
                i.putExtra("desc",d);
                i.putExtra("date",dt);
                i.putExtra("st",st);
                i.putExtra("et",et);
                i.putExtra("loc",l);
                i.putExtra("img",pathImage);
                startActivity(i);
            }
        });*/
    }
    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null)
            {
                Uri selectedImageURI = data.getData();
               File imageFile = new File(getRealPathFromURI(selectedImageURI));
               pathImage=getRealPathFromURI(selectedImageURI);
                ImageView imageView= (ImageView) findViewById(R.id.img_event);
                Picasso.with(this).load(selectedImageURI).resize(400,400).centerCrop().into(imageView, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        System.out.println("loaded Image");
                    }
                    @Override
                    public void onError()
                    {
                        System.out.println("Unable to load Image");
                    }
                });

            }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this,data);
                String toastMsg = String.format("%s", place.getName());
                loc.setText(toastMsg);
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
    public String getRealPathFromURI(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        Log.d("Path is:",path);
        return path;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
