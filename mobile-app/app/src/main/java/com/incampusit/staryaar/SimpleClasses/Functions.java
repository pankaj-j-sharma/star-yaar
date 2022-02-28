package com.incampusit.staryaar.SimpleClasses;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.util.Path;
import com.incampusit.staryaar.Comments.Comment_Get_Set;
import com.incampusit.staryaar.Comments.Comment_Like_Get_Set;
import com.incampusit.staryaar.Comments.Comment_Reply_Get_Set;
import com.incampusit.staryaar.Home.Home_Get_Set;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.Video_Recording.GallerySelectedVideo.GallerySelectedVideo_A;
import com.incampusit.staryaar.argear.FileDeleteAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.WritableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.incampusit.staryaar.videotrim.utils.UIThreadUtil.runOnUiThread;

/*
 * Created by PANKAJ on 2/20/2019.
 */

public class Functions {


    private static final int MY_REQUEST_CODE = 762;
    public static Dialog dialog;
    public static Dialog indeterminant_dialog;
    public static Dialog determinant_dialog;
    public static ProgressBar determinant_progress;
    public static Pattern userhandlePattern = Pattern.compile("@[a-zA-Z0-9]\\w+");

    public static SpannableStringBuilder getBoldUserHandle(String text) {
        SpannableStringBuilder userhandlespan = new SpannableStringBuilder();
        for (String word : text.split(" ")) {
            SpannableStringBuilder span = new SpannableStringBuilder(word);
            if (word.startsWith("@")) {
                span.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            span.append(" ");
            userhandlespan.append(span);
        }
        return userhandlespan;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void Show_Alert(Context context, String title, String Message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Message)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public static void Show_loader(Context context, boolean outside_touch, boolean cancleable) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_loading_view);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.d_round_white_background));


        CamomileSpinner loader = dialog.findViewById(R.id.loader);
        loader.start();


        if (!outside_touch)
            dialog.setCanceledOnTouchOutside(false);

        if (!cancleable)
            dialog.setCancelable(false);

        dialog.show();
    }

    public static void cancel_loader() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    public static float dpToPx(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public static void Share_through_app(final Activity activity, final String link) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, link);
                activity.startActivity(Intent.createChooser(intent, ""));

            }
        }).start();
    }

    public static Bitmap Uri_to_bitmap(Activity activity, Uri uri) {
        InputStream imageStream = null;
        try {
            imageStream = activity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

        String path = uri.getPath();
        Matrix matrix = new Matrix();
        ExifInterface exif = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                exif = new ExifInterface(path);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }


    // Bottom is all the Apis which is mostly used in app we have add it
    // just one time and whenever we need it we will call it

    public static String Bitmap_to_base64(Activity activity, Bitmap imagebitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArray = baos.toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }

    public static String Uri_to_base64(Activity activity, Uri uri) {
        InputStream imageStream = null;
        try {
            imageStream = activity.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

        String path = uri.getPath();
        Matrix matrix = new Matrix();
        ExifInterface exif = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                exif = new ExifInterface(path);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArray = baos.toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }

    public static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
        double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
        long currentSample = 0;
        double currentTime = 0;
        for (int i = 0; i < track.getSampleDurations().length; i++) {
            long delta = track.getSampleDurations()[i];

            if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
                timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
            }
            currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
            currentSample++;

        }
        double previous = 0;
        for (double timeOfSyncSample : timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                if (next) {
                    return timeOfSyncSample;
                } else {
                    return previous;
                }
            }
            previous = timeOfSyncSample;
        }
        return timeOfSyncSamples[timeOfSyncSamples.length - 1];
    }

    public static void Call_Api_For_like_video(final Activity activity,
                                               String video_id, String action,
                                               final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("video_id", video_id);
            parameters.put("action", action);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(activity, Variables.likeDislikeVideo, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                api_callBack.OnSuccess(resp);
            }
        });


    }

    public static void Call_Api_For_Send_Report_Content(final Activity activity, Home_Get_Set item, String comments, String option, final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("report_option", option);
            if (!comments.trim().isEmpty()) {
                parameters.put("report_comments", comments);
            }
            parameters.put("report_fb_id", item.fb_id);
            parameters.put("report_video_id", item.video_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(activity, Variables.postContentReport, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                try {
                    Log.d(Variables.TAG, "reposnse str " + resp);
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        String response_msg = response.optString("msg");
                        api_callBack.OnSuccess(response_msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void Call_Api_For_Send_Likes(final Activity activity, String comment_id, String type, final API_CallBack api_callBack) {
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("comment_id", comment_id);
            parameters.put("like_type", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(activity, Variables.likedislikeComments, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                ArrayList<Comment_Like_Get_Set> arrayList = new ArrayList<>();
                try {
                    Log.d(Variables.TAG, "reposnse str " + resp);
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        JSONObject msgObj = response.getJSONObject("msg");
                        JSONArray msgArray = msgObj.getJSONArray("likes");
                        for (int i = 0; i < msgArray.length(); i++) {
                            JSONObject itemdata = msgArray.optJSONObject(i);
                            Comment_Like_Get_Set item = new Comment_Like_Get_Set();
                            item.fb_id = itemdata.optString("fb_id");
                            item.user_liked = item.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id, "0"));
                            item.user_handle = itemdata.optString("handle");
                            item.first_name = itemdata.optString("first_name");
                            item.last_name = itemdata.optString("last_name");
                            item.user_handle = itemdata.optString("user_handle");
                            item.profile_pic = itemdata.optString("profile_pic");
                            arrayList.add(item);
                        }
                    } else {
                        Toast.makeText(activity, response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    api_callBack.ArrayData(arrayList);
                } catch (Exception e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }
            }
        });


    }

    public static void Call_Api_For_Send_Comment(final Activity activity, String video_id, String comment, String comment_id, final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("video_id", video_id);
            if (!comment_id.trim().isEmpty())
                parameters.put("comment_id", comment_id);
            parameters.put("comment", comment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(activity, Variables.postCommentsReply, parameters, new Callback() {
            @Override
            public void Responce(String resp) {

                ArrayList<Comment_Get_Set> arrayList = new ArrayList<>();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        JSONArray msgArray = response.getJSONArray("msg");
                        for (int i = 0; i < msgArray.length(); i++) {
                            JSONObject itemdata = msgArray.optJSONObject(i);

                            ArrayList<Comment_Reply_Get_Set> replyList = new ArrayList<>();
                            JSONArray replydata = itemdata.optJSONArray("replies");
                            for (int j = 0; j < replydata.length(); j++) {
                                Comment_Reply_Get_Set reply = new Comment_Reply_Get_Set();
                                JSONObject replyObj = replydata.optJSONObject(j);

                                reply.fb_id = replyObj.optString("fb_id");
                                reply.comment_id = replyObj.optString("reply_id");
                                reply.comments = replyObj.optString("comments");
                                reply.created = replyObj.optString("created");

                                JSONObject reply_user_info = replyObj.optJSONObject("user_info");
                                reply.user_handle = reply_user_info.optString("handle");
                                reply.first_name = reply_user_info.optString("first_name");
                                reply.last_name = reply_user_info.optString("last_name");
                                reply.profile_pic = reply_user_info.optString("profile_pic");

                                // populate reply like info
                                ArrayList<Comment_Like_Get_Set> likeList = new ArrayList<>();
                                JSONArray reply_like_info = replyObj.optJSONArray("reply_likes");
                                for (int k = 0; k < reply_like_info.length(); k++) {
                                    Comment_Like_Get_Set replyLike = new Comment_Like_Get_Set();
                                    JSONObject likeObj = reply_like_info.optJSONObject(k);

                                    replyLike.fb_id = likeObj.optString("fb_id");
                                    reply.user_liked = replyLike.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id, "0"));
                                    replyLike.user_handle = likeObj.optString("handle");
                                    replyLike.first_name = likeObj.optString("first_name");
                                    replyLike.last_name = likeObj.optString("last_name");
                                    replyLike.user_handle = likeObj.optString("user_handle");
                                    replyLike.profile_pic = likeObj.optString("profile_pic");

                                    likeList.add(replyLike);
                                }
                                reply.likes = String.valueOf(likeList.size());
                                reply.likesList = likeList;
                                // populate reply like info

                                replyList.add(reply);
                            }

                            Comment_Get_Set item = new Comment_Get_Set();
                            item.fb_id = itemdata.optString("fb_id");

                            JSONObject user_info = itemdata.optJSONObject("user_info");
                            item.first_name = user_info.optString("first_name");
                            item.last_name = user_info.optString("last_name");
                            item.profile_pic = user_info.optString("profile_pic");

                            // populate like info for main comment
                            ArrayList<Comment_Like_Get_Set> commentlikeList = new ArrayList<>();
                            JSONArray comment_like_info = itemdata.optJSONArray("likes");
                            for (int k = 0; k < comment_like_info.length(); k++) {
                                Comment_Like_Get_Set commentLike = new Comment_Like_Get_Set();
                                JSONObject commentlikeObj = comment_like_info.optJSONObject(k);

                                commentLike.fb_id = commentlikeObj.optString("fb_id");
                                //commentLike.user_liked = true;
                                //commentLike.user_liked = false;
                                item.user_liked = commentLike.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id, "0"));
                                commentLike.user_handle = commentlikeObj.optString("handle");
                                commentLike.first_name = commentlikeObj.optString("first_name");
                                commentLike.last_name = commentlikeObj.optString("last_name");
                                commentLike.user_handle = commentlikeObj.optString("user_handle");
                                commentLike.profile_pic = commentlikeObj.optString("profile_pic");

                                commentlikeList.add(commentLike);
                            }
                            item.likes = String.valueOf(commentlikeList.size());
                            item.likesList = commentlikeList;
                            // populate like info for main comment

                            item.video_id = itemdata.optString("id");
                            item.comment_id = itemdata.optString("comment_id");
                            item.comments = itemdata.optString("comments");
                            item.created = itemdata.optString("created");

                            item.arrayList = replyList;
                            item.replies = String.valueOf(replyList.size());

                            arrayList.add(item);
                        }

                        api_callBack.ArrayData(arrayList);

                    } else {
                        Toast.makeText(activity, "" + response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }

            }
        });


    }

    public static void Call_Api_For_get_Comment(final Activity activity, String video_id, final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("video_id", video_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(activity, Variables.showVideoCommentsNew, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                ArrayList<Comment_Get_Set> arrayList = new ArrayList<>();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        JSONArray msgArray = response.getJSONArray("msg");
                        for (int i = 0; i < msgArray.length(); i++) {
                            ArrayList<Comment_Reply_Get_Set> replyList = new ArrayList<>();

                            JSONObject itemdata = msgArray.optJSONObject(i);

                            JSONArray replydata = itemdata.optJSONArray("replies");
                            for (int j = 0; j < replydata.length(); j++) {
                                Comment_Reply_Get_Set reply = new Comment_Reply_Get_Set();
                                JSONObject replyObj = replydata.optJSONObject(j);

                                reply.fb_id = replyObj.optString("fb_id");
                                reply.comment_id = replyObj.optString("reply_id");
                                reply.comments = replyObj.optString("comments");
                                reply.created = replyObj.optString("created");

                                JSONObject reply_user_info = replyObj.optJSONObject("user_info");
                                reply.user_handle = reply_user_info.optString("handle");
                                reply.first_name = reply_user_info.optString("first_name");
                                reply.last_name = reply_user_info.optString("last_name");
                                reply.profile_pic = reply_user_info.optString("profile_pic");

                                // populate reply like info
                                ArrayList<Comment_Like_Get_Set> likeList = new ArrayList<>();
                                JSONArray reply_like_info = replyObj.optJSONArray("reply_likes");
                                for (int k = 0; k < reply_like_info.length(); k++) {
                                    Comment_Like_Get_Set replyLike = new Comment_Like_Get_Set();
                                    JSONObject likeObj = reply_like_info.optJSONObject(k);

                                    replyLike.fb_id = likeObj.optString("fb_id");
                                    reply.user_liked = replyLike.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id, "0"));
                                    replyLike.user_handle = likeObj.optString("handle");
                                    replyLike.first_name = likeObj.optString("first_name");
                                    replyLike.last_name = likeObj.optString("last_name");
                                    replyLike.user_handle = likeObj.optString("user_handle");
                                    replyLike.profile_pic = likeObj.optString("profile_pic");

                                    likeList.add(replyLike);
                                }
                                reply.likes = String.valueOf(likeList.size());
                                reply.likesList = likeList;
                                // populate reply like info

                                replyList.add(reply);
                            }

                            Comment_Get_Set item = new Comment_Get_Set();
                            item.fb_id = itemdata.optString("fb_id");

                            JSONObject user_info = itemdata.optJSONObject("user_info");
                            item.user_handle = user_info.optString("handle");
                            item.first_name = user_info.optString("first_name");
                            item.last_name = user_info.optString("last_name");
                            item.profile_pic = user_info.optString("profile_pic");

                            // populate like info for main comment
                            ArrayList<Comment_Like_Get_Set> commentlikeList = new ArrayList<>();
                            JSONArray comment_like_info = itemdata.optJSONArray("likes");
                            for (int k = 0; k < comment_like_info.length(); k++) {
                                Comment_Like_Get_Set commentLike = new Comment_Like_Get_Set();
                                JSONObject commentlikeObj = comment_like_info.optJSONObject(k);

                                commentLike.fb_id = commentlikeObj.optString("fb_id");
                                //commentLike.user_liked = true;
                                //commentLike.user_liked = false;
                                item.user_liked = commentLike.fb_id.equals(Variables.sharedPreferences.getString(Variables.u_id, "0"));
                                commentLike.user_handle = commentlikeObj.optString("handle");
                                commentLike.first_name = commentlikeObj.optString("first_name");
                                commentLike.last_name = commentlikeObj.optString("last_name");
                                commentLike.user_handle = commentlikeObj.optString("user_handle");
                                commentLike.profile_pic = commentlikeObj.optString("profile_pic");

                                commentlikeList.add(commentLike);
                            }
                            item.likes = String.valueOf(commentlikeList.size());
                            item.likesList = commentlikeList;
                            // populate like info for main comment

                            item.video_id = itemdata.optString("id");
                            item.comment_id = itemdata.optString("comment_id");
                            item.comments = itemdata.optString("comments");
                            item.created = itemdata.optString("created");

                            item.arrayList = replyList;
                            item.replies = String.valueOf(replyList.size());

                            arrayList.add(item);
                        }

                        api_callBack.ArrayData(arrayList);

                    } else {
                        Toast.makeText(activity, "" + response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }
            }
        });

    }

    public static void Call_Api_For_update_view(final Activity activity,
                                                String video_id) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("id", video_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Variables.TAG, "fb_id " + Variables.sharedPreferences.getString(Variables.u_id, "0"));
        ApiRequest.Call_Api(activity, Variables.updateVideoView, parameters, null);


    }

    public static void Call_Api_For_Follow_or_unFollow
            (final Activity activity,
             String fb_id,
             String followed_fb_id,
             String status,
             final API_CallBack api_callBack) {

        Functions.Show_loader(activity, false, false);


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fb_id);
            parameters.put("followed_fb_id", followed_fb_id);
            parameters.put("status", status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(activity, Variables.follow_users, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        api_callBack.OnSuccess(response.toString());

                    } else {
                        Toast.makeText(activity, "" + response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }
            }
        });


    }

    public static void Call_Api_For_Get_User_data
            (final Activity activity,
             String fb_id,
             final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fb_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("resp", parameters.toString());

        ApiRequest.Call_Api(activity, Variables.get_user_data, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        api_callBack.OnSuccess(response.toString());

                    } else {
                        Toast.makeText(activity, "" + response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }
            }
        });

    }

    public static void Call_Api_For_Delete_Video
            (final Activity activity,
             String video_id,
             final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("id", video_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(activity, Variables.DeleteVideo, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        if (api_callBack != null)
                            api_callBack.OnSuccess(response.toString());

                    } else {
                        Toast.makeText(activity, "" + response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    if (api_callBack != null)
                        api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }

            }
        });


    }

    public static void Show_indeterminent_loader(Context context, boolean outside_touch, boolean cancleable) {

        indeterminant_dialog = new Dialog(context);
        indeterminant_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        indeterminant_dialog.setContentView(R.layout.item_indeterminant_progress_layout);
        indeterminant_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.d_round_white_background));


        if (!outside_touch)
            indeterminant_dialog.setCanceledOnTouchOutside(false);

        if (!cancleable)
            indeterminant_dialog.setCancelable(false);

        indeterminant_dialog.show();

    }

    public static void cancel_indeterminent_loader() {
        if (indeterminant_dialog != null) {
            indeterminant_dialog.cancel();
        }
    }

    public static void Show_determinent_loader(Context context, boolean outside_touch, boolean cancleable) {

        determinant_dialog = new Dialog(context);
        determinant_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        determinant_dialog.setContentView(R.layout.item_determinant_progress_layout);
        determinant_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.d_round_white_background));

        determinant_progress = determinant_dialog.findViewById(R.id.pbar);

        if (!outside_touch)
            determinant_dialog.setCanceledOnTouchOutside(false);

        if (!cancleable)
            determinant_dialog.setCancelable(false);

        determinant_dialog.show();

    }

    public static void Show_loading_progress(int progress) {
        if (determinant_progress != null) {
            determinant_progress.setProgress(progress);

        }
    }


    public static void cancel_determinent_loader() {
        if (determinant_dialog != null) {
            determinant_progress = null;
            determinant_dialog.cancel();
        }
    }


    public static boolean Checkstoragepermision(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;

            } else {

                activity.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {

            return true;
        }
    }


    // these function are remove the cache memory which is very helpfull in memmory managmet
    public static void deleteCache(Context context) {
        Glide.get(context).clearMemory();

        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    // change the date into (today ,yesterday and date)
    public static String ChangeDate(String date) {
        String final_date = "";
        Calendar cal = Calendar.getInstance();
        int today_day = cal.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long currenttime = System.currentTimeMillis();

            //database date in millisecond
            long databasedate = 0;
            Date d = null;
            try {
                d = df.parse(date);
                databasedate = d.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference = currenttime - databasedate;
            if (difference < 86400000) {
                //int chatday = Integer.parseInt(date.substring(0, 2));
                int chatday = d.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if (today_day == chatday)
                    final_date = "Today at " + sdf.format(d);
                else if ((today_day - chatday) == 1)
                    final_date = "Yesterday at " + sdf.format(d);
            } else if (difference < 172800000) {
                int chatday = Integer.parseInt(date.substring(0, 2));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if ((today_day - chatday) == 1)
                    final_date = "Yesterday at " + sdf.format(d);
            }
            if (final_date == "") {
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM");
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                final_date = sdf1.format(d) + " at " + sdf2.format(d);
            }
        } catch (Exception e) {
            Log.d(Variables.TAG, "Error catch " + e.toString());
        } finally {
            Log.d(Variables.TAG, "finally " + final_date);
            return final_date;
        }

    }

    //method to convert your text to image
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    // methdo to share the video to app locally
    public static void Share_Video_local(String path, Context context) {
        Functions.cancel_determinent_loader();
        ContentValues content = new ContentValues(4);
        content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                System.currentTimeMillis() / 1000);
        content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        content.put(MediaStore.Video.Media.DATA, path);
        content.put(MediaStore.Video.Media.TITLE, "Star Yaar Video");

        ContentResolver resolver = context.getApplicationContext().getContentResolver();
        Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        String final_text = context.getResources().getString(R.string.share_deeplink) + "\n" + context.getResources().getString(R.string.share_video) + "\n\n" + context.getResources().getString(R.string.play_store);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.share_video));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, final_text);
        sharingIntent.putExtra(Intent.ACTION_ATTACH_DATA, "Split Name");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(sharingIntent, "Share Video"));
    }

    public static void startTrim(final Context context, final File src, final File dst, final int startMs, final int endMs) throws IOException {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {

                    FileDataSourceImpl file = new FileDataSourceImpl(src);
                    Movie movie = MovieCreator.build(file);
                    List<Track> tracks = movie.getTracks();
                    movie.setTracks(new LinkedList<Track>());
                    double startTime = startMs / 1000;
                    double endTime = endMs / 1000;
                    boolean timeCorrected = false;

                    for (Track track : tracks) {
                        if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                            if (timeCorrected) {
                                throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                            }
                            startTime = Functions.correctTimeToSyncSample(track, startTime, false);
                            endTime = Functions.correctTimeToSyncSample(track, endTime, true);
                            timeCorrected = true;
                        }
                    }
                    for (Track track : tracks) {
                        long currentSample = 0;
                        double currentTime = 0;
                        long startSample = -1;
                        long endSample = -1;

                        for (int i = 0; i < track.getSampleDurations().length; i++) {
                            if (currentTime <= startTime) {
                                startSample = currentSample;
                            }
                            if (currentTime <= endTime) {
                                endSample = currentSample;
                            } else {
                                break;
                            }
                            currentTime += (double) track.getSampleDurations()[i] / (double) track.getTrackMetaData().getTimescale();
                            currentSample++;
                        }
                        movie.addTrack(new CroppedTrack(track, startSample, endSample));
                    }

                    Container out = new DefaultMp4Builder().build(movie);
                    MovieHeaderBox mvhd = Path.getPath(out, "moov/mvhd");
                    mvhd.setMatrix(com.googlecode.mp4parser.util.Matrix.ROTATE_180);
                    if (!dst.exists()) {
                        dst.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(dst);
                    WritableByteChannel fc = fos.getChannel();
                    try {
                        out.writeContainer(fc);
                    } finally {
                        fc.close();
                        fos.close();
                        file.close();
                    }

                    file.close();
                    return "Ok";
                } catch (IOException e) {
                    return "error";
                }

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Functions.Show_indeterminent_loader(context, true, true);
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.equals("error")) {
                    Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                } else {
                    Functions.cancel_indeterminent_loader();
                    Chnage_Video_size(context, Variables.gallery_trimed_video, Variables.gallery_resize_video);
                }
            }


        }.execute();

    }

    public static void Chnage_Video_size(final Context context, String src_path, String destination_path) {

        Functions.Show_determinent_loader(context, false, false);
        new GPUMp4Composer(src_path, destination_path)
                //.size(720, 1280)
                //.videoBitrate((int) (0.25 * 16 * 540 * 960))
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {

                        Log.d("resp", "" + (int) (progress * 100));
                        Functions.Show_loading_progress((int) (progress * 100));

                    }

                    @Override
                    public void onCompleted() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Functions.cancel_determinent_loader();

                                Intent intent = new Intent(context, GallerySelectedVideo_A.class);
                                intent.putExtra("video_path", Variables.gallery_resize_video);
                                context.startActivity(intent);

                                // load video trimming option
                                /*
                                Intent intent=new Intent(GalleryVideos_A.this, ActVideoTrimmer.class);
                                intent.putExtra(TrimmerConstants.TRIM_VIDEO_URI,String.valueOf(Variables.gallery_resize_video));
                                Uri uri = Uri.parse(String.valueOf(Variables.gallery_resize_video));
                                intent.putExtra(TrimmerConstants.DESTINATION,"/storage/emulated/0/DCIM/MYFOLDER"); //optional default output path /storage/emulated/0/DOWNLOADS
                                startActivityForResult(intent, TrimmerConstants.REQ_CODE_VIDEO_TRIMMER);
                                 */
                                // load video trimming option
                                //startActivityForResult(VideoCropActivity.createIntent(GalleryVideos_A.this, String.valueOf(Variables.gallery_resize_video), "/storage/emulated/0/DCIM/MYFOLDER/output_crop.mp4"), TrimmerConstants.REQ_CODE_VIDEO_TRIMMER);
                            }
                        });


                    }

                    @Override
                    public void onCanceled() {
                        Log.d("resp", "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        Log.d("resp", exception.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Functions.cancel_determinent_loader();

                                    Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {

                                }
                            }
                        });

                    }
                })
                .start();
    }

    public static File getConvertedFile(File originalFile, String format) {
        String[] f = originalFile.getPath().split("\\.");
        String filePath = originalFile.getPath().replace(f[f.length - 1], format);
        return new File(filePath);
    }

    public static void clearRootDirContents(Context context) {
        String mInnerMediaPath = Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath() + "/ARGearMedia";
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(Variables.app_folder);
                for (File temp : file.listFiles()) {
                    temp.delete();
                }
            }
        }).start();

        new FileDeleteAsyncTask(new File(mInnerMediaPath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
            @Override
            public void processFinish(Object result) {
                File dir = new File(mInnerMediaPath);
                if (!dir.exists()) {
                    boolean r = dir.mkdir();
                    Log.e(Variables.TAG, "");
                }
            }
        }).execute();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File file = new File(mInnerMediaPath);
                        for (File temp : file.listFiles()) {
                            temp.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void LaunchSocialMedia(String type, Context context, String userid) {
        Log.d(Variables.TAG, "userid " + userid + " " + type);

        if (type.isEmpty() || userid.isEmpty()) {
            return;
        } else {
            if (type.toLowerCase().equals("facebook")) {
                String FACEBOOK_URL = "https://www.facebook.com/" + userid;
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                try {
                    String facebookUrl = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    facebookIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(facebookIntent);
                } catch (Exception e) {
                    Log.d(Variables.TAG, "Error launching in app " + e.toString());
                    String facebookUrl = FACEBOOK_URL;
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    context.startActivity(facebookIntent);
                }
            } else if (type.toLowerCase().equals("youtube")) {

                String YOUTUBE_URL = "https://www.youtube.com/" + userid;
                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);
                youtubeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                youtubeIntent.setData(Uri.parse(YOUTUBE_URL));
                context.startActivity(youtubeIntent);

            } else if (type.toLowerCase().equals("twitter")) {

                String TWITTER_URL = "https://twitter.com/" + userid;
                Intent twitterIntent = new Intent(Intent.ACTION_VIEW);
                try {
                    twitterIntent.setData(Uri.parse("twitter://user?user_id=" + userid));
                    twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(twitterIntent);
                } catch (Exception e) {
                    Log.d(Variables.TAG, "Error launching in app " + e.toString());
                    twitterIntent.setData(Uri.parse(TWITTER_URL));
                    context.startActivity(twitterIntent);
                }

            } else if (type.toLowerCase().equals("instagram")) {

                String INSTAGRAM_URL = "https://www.instagram.com/" + userid;
                Intent instagramIntent = new Intent(Intent.ACTION_VIEW);
                try {
                    instagramIntent.setData(Uri.parse(INSTAGRAM_URL));
                    instagramIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(instagramIntent);
                } catch (Exception e) {
                    Log.d(Variables.TAG, "Error launching in app " + e.toString());
                    instagramIntent.setData(Uri.parse(INSTAGRAM_URL));
                    context.startActivity(instagramIntent);
                }
            }
        }
    }

}
