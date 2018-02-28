package com.example.ae.yourhub;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostEventsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    int type=0;
    int PICK_IMAGE=10,PICK_CAMERA=5;
    ImageView imageView;
    ImageButton gallery,camera;
    EditText name, description, date, loc, starts, ends;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    String pathImage=" ";
    private RequestQueue mRequestQueue;
    private File output=null;
    String mCurrentPhotoPath,imageFileName;
    File photoFile=null;
    int PLACE_PICKER_REQUEST = 1;
    GoogleApiClient mGoogleApiClient;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_insert);

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

        mGoogleApiClient = new GoogleApiClient
                .Builder( this )
                .enableAutoManage( this, 0, this )
                .addApi( Places.GEO_DATA_API )
                .addApi( Places.PLACE_DETECTION_API )
                .addConnectionCallbacks( PostEventsActivity.this )
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
                DatePickerDialog d=new DatePickerDialog(PostEventsActivity.this, date2, 2017,03,11);
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
                new TimePickerDialog(PostEventsActivity.this,timeListener ,0,0, DateFormat.is24HourFormat(PostEventsActivity.this)).show();
            }
        });
        ends.setFocusable(false);
        ends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(PostEventsActivity.this,timeListener2 ,0,0, DateFormat.is24HourFormat(PostEventsActivity.this)).show();
            }
        });
        linearLayout=(LinearLayout) findViewById(R.id.l1);
        imageView=(ImageView) findViewById(R.id.photo);
        imageView.setOnClickListener(new View.OnClickListener() {
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
                    startActivityForResult(builder.build(PostEventsActivity.this), PLACE_PICKER_REQUEST);
                }
                catch ( GooglePlayServicesRepairableException e ) {
                    Log.d( "PlacesAPI Demo", "GooglePlayServicesRepairableException thrown" );}
                catch ( GooglePlayServicesNotAvailableException e ) {
                    Log.d( "PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown" );
                }

            }
        });

        gallery=(ImageButton) findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMediaPermissionGranted())
                {
                    accessGallery();;
                }

            }
        });
        camera=(ImageButton) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStoragePermissionGranted())
                {
                    takepic();
                }

            }
        });
        progressDialog=new ProgressDialog(this,ProgressDialog.STYLE_SPINNER);
    }
    /***Option Menu Creation and Actions***/
    /*-----------------------------------*/

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
            String n=name.getText().toString();
            String d=description.getText().toString();
            String dt=date.getText().toString();
            String l=loc.getText().toString();
            String st=starts.getText().toString();
            String et=ends.getText().toString();


            Bitmap image=((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            EventDatabase eventDatabase=new EventDatabase(PostEventsActivity.this,n,d,dt,l,st,et,encodeImage);
            if(eventDatabase.insert_data())
            {
                Intent intent=new Intent(PostEventsActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    /***Gallery Access***/
    /*------------------*/
    public void accessGallery()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    /***Take Photo Code***/
    /*----------------------*/
    public void takepic()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(PostEventsActivity.this,
                        "com.example.yourhub.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,PICK_CAMERA);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.e("hi","im here");
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("path:",mCurrentPhotoPath);
        return image;
    }

    /***Intent Request Returns***/
    /*---------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageURI = data.getData();
            Picasso.with(this).load(selectedImageURI).resize(400, 400).centerCrop().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    System.out.println("loaded Image");
                }

                @Override
                public void onError() {
                    System.out.println("Unable to load Image");
                }
            });

        }
        if (requestCode == PICK_CAMERA && resultCode == RESULT_OK) {


            Picasso.with(this).load(new File(mCurrentPhotoPath)).resize(400,400).centerCrop().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    System.out.println("loaded Image");
                    Log.e("picasso","loaded");
                }

                @Override
                public void onError() {
                    Log.e("picasso","not loaded");
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



    /***Permission Requests***/
    /*----------------------*/
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSION","Permission is granted");
                return true;
            } else {

                Log.v("PERMISSION","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 23);
                return false;
            }
        }
        else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v("PERMISSION","Permission is granted");
            return true;
        }
    }
    public  boolean isMediaPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSION","Permission is granted");
                return true;
            } else {

                Log.v("PERMISSION","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 24);
                return false;
            }
        }
        else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v("PERMISSION","Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==23 && grantResults!=null)
        {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v("PERMISSION", "Permission is granted");
                    takepic();
                }
            }
        }
        if(requestCode==24 && grantResults!=null)
        {
            if (Build.VERSION.SDK_INT >= 24) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v("Media PERMISSION", "Permission is granted");
                    accessGallery();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
