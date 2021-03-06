package com.example.ae.yourhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostImageActivity extends AppCompatActivity {

    int type=0;
    int PICK_IMAGE=10,PICK_CAMERA=5;
    ImageView imageView;
    ImageButton gallery,camera;
    EditText title,desc;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    String pathImage=" ";
    private RequestQueue mRequestQueue;
    private File output=null;
    String mCurrentPhotoPath,imageFileName;
    File photoFile=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);

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

        title=(EditText) findViewById(R.id.title);
        desc=(EditText) findViewById(R.id.desc);
        linearLayout=(LinearLayout) findViewById(R.id.l1);
        imageView=(ImageView) findViewById(R.id.photo);

        Session session=new Session(this);
        if(session.getid().equals(""))
        {
           // Intent intent=new Intent(PostImageActivity.this,UserActivity.class);
            //startActivity(intent);
        }

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
            String t=title.getText().toString();
            String d=desc.getText().toString();
            Bitmap image=((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
            ForumDatabase fd=new ForumDatabase(PostImageActivity.this,t,d,encodeImage);
            if(fd.insert_image())
            {
                progressDialog.dismiss();
                Intent i=new Intent(PostImageActivity.this,HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
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
                Uri photoURI = FileProvider.getUriForFile(PostImageActivity.this,
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


}
