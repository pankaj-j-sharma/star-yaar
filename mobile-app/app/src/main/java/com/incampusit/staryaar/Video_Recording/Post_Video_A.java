package com.incampusit.staryaar.Video_Recording;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.incampusit.staryaar.Main_Menu.MainMenuActivity;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.Services.ServiceCallback;
import com.incampusit.staryaar.Services.Upload_Service;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Post_Video_A extends AppCompatActivity implements ServiceCallback {


    ImageView video_thumbnail;

    RadioButton btnprivate, btnpublic, btnrestricted;

    String video_path, privacy_option;

    ProgressDialog progressDialog;

    ServiceCallback serviceCallback;


    EditText description_edit;
    // this is importance for binding the service to the activity
    Upload_Service mService;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            Upload_Service.LocalBinder binder = (Upload_Service.LocalBinder) service;
            mService = binder.getService();

            mService.setCallbacks(Post_Video_A.this);


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_video);

        video_path = Variables.output_filter_file;
        video_path = getIntent().getStringExtra("outputFilePath");
        Log.d(Variables.TAG, "video_path -> " + video_path);

        video_thumbnail = findViewById(R.id.video_thumbnail);
        description_edit = findViewById(R.id.description_edit);

        btnprivate = findViewById(R.id.post_private_btn);
        btnrestricted = findViewById(R.id.post_restricted_btn);
        btnpublic = findViewById(R.id.post_public_btn);

        btnpublic.setChecked(true);

        // create the gif file in the background as it will take some time to generate
        new Thread(new Runnable() {
            @Override
            public void run() {
                createGIFFile(video_path);
            }
        }).start();

        // this will get the thumbnail of video and show them in imageview
        Bitmap bmThumbnail;
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(video_path,
                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

        if (bmThumbnail != null) {
            video_thumbnail.setImageBitmap(bmThumbnail);
        } else {
        }


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);


        findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        findViewById(R.id.post_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                // get the privacy option for video
                if (btnprivate.isChecked())
                    privacy_option = "PRIVATE";
                else if (btnrestricted.isChecked())
                    privacy_option = "RESTRICTED";
                else
                    privacy_option = "PUBLIC";

                Start_Service();

            }
        });


    }

    // this will start the service for uploading the video into database
    public void Start_Service() {

        serviceCallback = this;

        Upload_Service mService = new Upload_Service(serviceCallback);
        if (!Functions.isMyServiceRunning(this, mService.getClass())) {
            Intent mServiceIntent = new Intent(this.getApplicationContext(), mService.getClass());
            mServiceIntent.setAction("startservice");
            mServiceIntent.putExtra("uri", "" + Uri.fromFile(new File(video_path)));
            mServiceIntent.putExtra("desc", "" + description_edit.getText().toString());
            mServiceIntent.putExtra("privacy", "" + privacy_option);
            //Log.d(Variables.TAG,"privacy post "+privacy_option);
            startService(mServiceIntent);


            Intent intent = new Intent(this, Upload_Service.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        } else {
            Toast.makeText(this, "Please wait video already in uploading progress", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        Stop_Service();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    // when the video is uploading successfully it will restart the appliaction
    @Override
    public void ShowResponce(final String responce) {

        Toast.makeText(Post_Video_A.this, responce, Toast.LENGTH_LONG).show();
        progressDialog.dismiss();

        if (responce.equalsIgnoreCase("Your Video is uploaded Successfully")) {

            startActivity(new Intent(Post_Video_A.this, MainMenuActivity.class));
            finishAffinity();
            try {
                Functions.clearRootDirContents(this);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(Variables.TAG, "Error on clearing files " + e.toString());
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    // this function will stop the the ruuning service
    public void Stop_Service() {

        serviceCallback = this;

        Upload_Service mService = new Upload_Service(serviceCallback);

        if (Functions.isMyServiceRunning(this, mService.getClass())) {
            Intent mServiceIntent = new Intent(this.getApplicationContext(), mService.getClass());
            mServiceIntent.setAction("stopservice");
            startService(mServiceIntent);

        }


    }

    private Uri createGIFFile(String path) {

        final MediaMetadataRetriever mmRetriever = new MediaMetadataRetriever();
        mmRetriever.setDataSource(path);

        final ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
        for (int i = 1000000; i < 2000 * 1000; i += 100000) {
            Bitmap bitmap = mmRetriever.getFrameAtTime(i, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.4), (int) (bitmap.getHeight() * 0.4), true);
            bitmaps.add(resized);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        for (Bitmap bitmap : bitmaps) {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            encoder.addFrame(decoded);

        }

        encoder.finish();

        File gifFile = new File(Variables.app_folder, "sample.gif");
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(gifFile);
            outputStream.write(bos.toByteArray());
            Upload_Service.isGifGenerated = true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return Uri.parse(gifFile.getPath());
    }

}
