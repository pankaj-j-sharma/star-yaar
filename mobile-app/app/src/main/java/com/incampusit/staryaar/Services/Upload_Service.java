package com.incampusit.staryaar.Services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.incampusit.staryaar.Main_Menu.MainMenuActivity;
import com.incampusit.staryaar.Retrofit.ApiConfig;
import com.incampusit.staryaar.Retrofit.AppConfig;
import com.incampusit.staryaar.Retrofit.ServerResponse;
import com.incampusit.staryaar.SimpleClasses.Variables;
import com.incampusit.staryaar.Video_Recording.AnimatedGifEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/*
 * Created by PANKAJ on 6/7/2018.
 */


// this the background service which will upload the video into database
public class Upload_Service extends Service {


    public static boolean isGifGenerated = false;
    private final IBinder mBinder = new LocalBinder();
    public long startTime;
    boolean mAllowRebind;
    ServiceCallback Callback;
    Uri uri;
    String video_base64 = "", thumb_base_64 = "", Gif_base_64 = "";
    String description, privacy;
    SharedPreferences sharedPreferences;

    public Upload_Service() {
        super();
    }

    public Upload_Service(ServiceCallback serviceCallback) {
        Callback = serviceCallback;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    private static MultipartBody.Part toMultipartBody(File fileParam, String name) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), fileParam);
        return MultipartBody.Part.createFormData(name, fileParam.getName(), requestBody);
    }

    private static RequestBody toRequestBody(String textParam) {
        return RequestBody.create(MediaType.parse("text/plain"), textParam);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    public void setCallbacks(ServiceCallback serviceCallback) {
        Callback = serviceCallback;
    }

    @Override
    public void onCreate() {
        sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);
    }

    //@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction().equals("startservice")) {
                showNotification();
                uri = Uri.parse(intent.getStringExtra("uri"));
                description = intent.getStringExtra("desc");
                privacy = intent.getStringExtra("privacy");
                //Log.d(Variables.TAG,"privacy upload "+privacy);

                // measure performance
                startTime = System.currentTimeMillis();
                Log.d("MEASUREPERF", "Beginning evaluation ..." + (System.currentTimeMillis()));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(uri.getPath(),
                                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                        Uri gifPath;
                        if (isGifGenerated)
                            gifPath = Uri.parse(Variables.app_folder + "sample.gif");
                        else
                            gifPath = createGIFFile(uri.getPath());

                        Uri thumbNailPath = createThumbnailFile(bmThumbnail);

                        HashMap<String, String> parameters = new HashMap<>();
                        parameters.put("fb_id", sharedPreferences.getString(Variables.u_id, ""));
                        parameters.put("sound_id", Variables.Selected_sound_id);
                        parameters.put("description", description);
                        parameters.put("privacy", privacy);
                        parameters.put("video", uri.getPath());
                        parameters.put("thumbnail", thumbNailPath.getPath());
                        parameters.put("gif", gifPath.getPath());

                        Log.d("MEASUREPERF", "Step 1 " + (System.currentTimeMillis() - startTime));
                        uploadVideoRetrofit2(getBaseContext(), parameters);
                    }
                }).start();

            } else if (intent.getAction().equals("stopservice")) {
                stopForeground(true);
                stopSelf();
            }

        }

        return Service.START_STICKY;
    }

    private Uri createThumbnailFile(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
        File thumbNail = new File(Variables.app_folder, "thumbnail.jpeg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(thumbNail);
            fileOutputStream.write(out.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            return Uri.parse(Variables.app_folder);
        }
        return Uri.parse(thumbNail.getPath());
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
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return Uri.parse(gifFile.getPath());
    }

    public boolean uploadVideoRetrofit2(final Context context, HashMap<String, String> uploadParameters) {
        boolean uploaded = false;

        // build request parameters
        File videoFile = new File(uploadParameters.get("video"));
        File gifFile = new File(uploadParameters.get("gif"));
        File thumbNailFile = new File(uploadParameters.get("thumbnail"));

        HashMap<String, RequestBody> requestmap = new HashMap<>();
        requestmap.put("fb_id", toRequestBody(uploadParameters.get("fb_id")));
        requestmap.put("sound_id", toRequestBody(uploadParameters.get("sound_id")));
        requestmap.put("description", toRequestBody(uploadParameters.get("description")));
        requestmap.put("privacy", toRequestBody(uploadParameters.get("privacy")));
        Log.d(Variables.TAG, "privacy retro " + uploadParameters.get("privacy"));

        MultipartBody.Part videoToUpload = toMultipartBody(videoFile, "video");
        MultipartBody.Part gifToUpload = toMultipartBody(gifFile, "gif");
        MultipartBody.Part thmbToUpload = toMultipartBody(thumbNailFile, "thumbnail");

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> call = getResponse.uploadMultipartVideo(videoToUpload, gifToUpload, thmbToUpload, requestmap);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                Log.d("MEASUREPERF", "Step 2 " + (System.currentTimeMillis() - startTime));
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Log.d("MEASUREPERF", serverResponse.getMessage());
                        Callback.ShowResponce("Your Video is uploaded Successfully");
                        stopForeground(true);
                        stopSelf();
                    } else {
                        Log.d("MEASUREPERF", "Retrofit not success " + (System.currentTimeMillis()) + serverResponse.getMessage());
                        Callback.ShowResponce("An error occurred Please Try Later");
                        stopForeground(true);
                        stopSelf();
                    }
                } else {
                    Log.d("MEASUREPERF", "Retrofit response null " + (System.currentTimeMillis()));
                    Callback.ShowResponce("An error occurred Please Try Later");
                    stopForeground(true);
                    stopSelf();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d("MEASUREPERF", "Retrofit resp error " + (System.currentTimeMillis()) + t.toString());
                Callback.ShowResponce("Their is some kind of problem from Server side Please Try Later");
                stopForeground(true);
                stopSelf();
            }
        });
        return uploaded;
    }

    //@Override
    public int onStartCommandOld(Intent intent, int flags, int startId) {

        if (intent != null) {
            if (intent.getAction().equals("startservice")) {
                showNotification();

                final String uri_string = intent.getStringExtra("uri");
                uri = Uri.parse(uri_string);
                description = intent.getStringExtra("desc");

                // measure performance
                final long startTime = System.currentTimeMillis();
                //MainClass.uploadFile(uri.getPath(),getBaseContext(),startTime);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MEASUREPERF", "Beginning evaluation ..." + (System.currentTimeMillis() - startTime));
                        //MainClass.uploadFile(uri.getPath(),getBaseContext(),startTime);

                        Bitmap bmThumbnail;
                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(uri.getPath(),
                                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                        //Bitmap bmThumbnail_resized = Bitmap.createScaledBitmap(bmThumbnail, (int) (bmThumbnail.getWidth() * 0.4), (int) (bmThumbnail.getHeight() * 0.4), true);

                        thumb_base_64 = Bitmap_to_base64(bmThumbnail);

                        try {
                            video_base64 = encodeFileToBase64Binary(uri);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Log.d("MEASUREPERF", "Encoded video audio " + (System.currentTimeMillis() - startTime));

                        File myVideo = new File(uri.getPath());
                        Uri myVideoUri = Uri.parse(myVideo.toString());

                        final MediaMetadataRetriever mmRetriever = new MediaMetadataRetriever();
                        mmRetriever.setDataSource(myVideo.getAbsolutePath());

                        //final MediaPlayer mp = MediaPlayer.create(getBaseContext(), myVideoUri);

                        final ArrayList<Bitmap> frames = new ArrayList<Bitmap>();

                        for (int i = 1000000; i < 2000 * 1000; i += 100000) {
                            Bitmap bitmap = mmRetriever.getFrameAtTime(i, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.4), (int) (bitmap.getHeight() * 0.4), true);
                            frames.add(resized);
                        }

                        Gif_base_64 = Base64.encodeToString(generateGIF(frames), Base64.DEFAULT);

                        Log.d("MEASUREPERF", "Created gif.. " + (System.currentTimeMillis() - startTime));

                        JSONObject parameters = new JSONObject();

                        try {
                            parameters.put("fb_id", sharedPreferences.getString(Variables.u_id, ""));
                            parameters.put("sound_id", Variables.Selected_sound_id);
                            parameters.put("description", description);

                            JSONObject vidoefiledata = new JSONObject();
                            vidoefiledata.put("file_data", video_base64);
                            parameters.put("videobase64", vidoefiledata);


                            JSONObject imagefiledata = new JSONObject();
                            imagefiledata.put("file_data", thumb_base_64);
                            parameters.put("picbase64", imagefiledata);


                            JSONObject giffiledata = new JSONObject();
                            giffiledata.put("file_data", Gif_base_64);
                            parameters.put("gifbase64", giffiledata);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //generateNoteOnSD("parameters", parameters.toString());

                        RequestQueue rq = Volley.newRequestQueue(Upload_Service.this);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.POST, Variables.uploadVideo, parameters, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        String respo = response.toString();
                                        Log.d("MEASUREPERF", "Response recieved " + (System.currentTimeMillis() - startTime));

                                        Log.d("responce", respo);

                                        Callback.ShowResponce("Your Video is uploaded Successfully");
                                        stopForeground(true);
                                        stopSelf();


                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("respo", error.toString());

                                        Callback.ShowResponce("Their is some kind of problem from Server side Please Try Later");
                                        stopForeground(true);
                                        stopSelf();

                                    }
                                });

                        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(120000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        rq.getCache().clear();
                        rq.add(jsonObjectRequest);

                        Log.d("MEASUREPERF", "Request created..  " + (System.currentTimeMillis() - startTime));

                    }
                }).start();

            } else if (intent.getAction().equals("stopservice")) {
                stopForeground(true);
                stopSelf();
            }

        }


        return Service.START_STICKY;
    }


    // this will show the sticky notification during uploading video
    private void showNotification() {

        Intent notificationIntent = new Intent(this, MainMenuActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        final String CHANNEL_ID = "default";
        final String CHANNEL_NAME = "Default";

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel defaultChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(defaultChannel);
        }

        androidx.core.app.NotificationCompat.Builder builder = new androidx.core.app.NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Uploading Video")
                .setContentText("Please wait! Video is uploading....")
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        android.R.drawable.stat_sys_upload))
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        startForeground(101, notification);
    }


    // for thumbnail
    public String Bitmap_to_base64(Bitmap imagebitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] byteArray = baos.toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }


    // for video base64
    private String encodeFileToBase64Binary(Uri fileName)
            throws IOException {

        File file = new File(fileName.getPath());
        byte[] bytes = loadFile(file);
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encodedString;
    }

    //for video gif image
    public byte[] generateGIF(ArrayList<Bitmap> bitmaps) {

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


        File filePath = new File(Variables.root, "sample.gif");
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(filePath);
            outputStream.write(bos.toByteArray());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return bos.toByteArray();
    }

    public void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName + ".txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class LocalBinder extends Binder {
        public Upload_Service getService() {
            return Upload_Service.this;
        }
    }


}