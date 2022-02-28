package com.incampusit.staryaar.Video_Recording.GalleryVideos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.util.Matrix;
import com.googlecode.mp4parser.util.Path;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;
import com.incampusit.staryaar.VideoCrop.VideoCropActivity;
import com.incampusit.staryaar.VideoTrimmer.utils.TrimmerConstants;
import com.incampusit.staryaar.Video_Recording.GallerySelectedVideo.GallerySelectedVideo_A;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GalleryVideos_A extends AppCompatActivity {

    public RecyclerView recyclerView;
    ArrayList<GalleryVideo_Get_Set> data_list;
    GalleryVideos_Adapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_videos);

        recyclerView = findViewById(R.id.recylerview);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        data_list = new ArrayList<>();
        getAllVideoPathRunnable(this);

        adapter = new GalleryVideos_Adapter(this, data_list, new GalleryVideos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, GalleryVideo_Get_Set item, View view) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                Bitmap bmp = null;
                try {
                    retriever.setDataSource(item.video_path);
                    bmp = retriever.getFrameAtTime();
                    int videoHeight = bmp.getHeight();
                    int videoWidth = bmp.getWidth();

                    Log.d("resp", "" + videoWidth + "---" + videoHeight);

                } catch (Exception e) {

                }

                checkToTrimorUseOriginal(item);
                /*
                if (item.video_duration_ms < 19500) {
                    Chnage_Video_size(item.video_path, Variables.gallery_resize_video);

                } else {
                    try {
                        startTrim(new File(item.video_path), new File(Variables.gallery_trimed_video), 1000, 18000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                 */

            }
        });

        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        //getAllVideoPath(this);


        findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);

            }
        });

    }


    public void getAllVideoPath(Context context) {

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};
        //Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        // code to optimze load
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                GalleryVideo_Get_Set item = new GalleryVideo_Get_Set();
                item.video_path = cursor.getString(0);
                item.video_duration_ms = getfileduration(Uri.parse(cursor.getString(0)));

                Log.d("resp", "" + item.video_duration_ms);

                if (item.video_duration_ms > 5000) {
                    item.video_time = change_sec_to_time(item.video_duration_ms);
                    data_list.add(item);
                }

            }
            adapter.notifyDataSetChanged();
            cursor.close();
        }

    }

    public void getAllVideoPathRunnable(final Context context) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading Media from your gallery ..");
        progressDialog.show();
        progressDialog.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] projection = {MediaStore.Video.VideoColumns.DATA};
                Log.d(Variables.TAG, "Path " + uri.getPath() + " " + Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media").getPath());
                Cursor mediacursor = context.getContentResolver().query(uri, projection, null, null, MediaStore.Video.VideoColumns.DATE_MODIFIED + " DESC");
                Cursor whatsappcursor = context.getContentResolver().query(Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media"), projection, null, null, MediaStore.Video.VideoColumns.DATE_MODIFIED + " DESC");
                // code to optimze load
                //CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
                //Cursor cursor = loader.loadInBackground();

                if (mediacursor != null) {
                    while (mediacursor.moveToNext()) {
                        //Log.d(Variables.TAG,"media cursor "+System.currentTimeMillis());
                        GalleryVideo_Get_Set item = new GalleryVideo_Get_Set();
                        item.video_path = mediacursor.getString(0);
                        item.video_duration_ms = getfileduration(Uri.parse(mediacursor.getString(0)));
                        Log.d("resp", "" + item.video_duration_ms);
                        if (item.video_duration_ms > 5000) {
                            item.video_time = change_sec_to_time(item.video_duration_ms);
                            data_list.add(item);
                            progressDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyItemInserted(data_list.size());
                                }
                            });
                        }
                    }
                    mediacursor.close();
                }
                // load whatsapp media from gallery
                if (whatsappcursor != null) {
                    while (whatsappcursor.moveToNext()) {
                        //Log.d(Variables.TAG,"whatsapp cursor "+System.currentTimeMillis());
                        GalleryVideo_Get_Set item = new GalleryVideo_Get_Set();
                        item.video_path = whatsappcursor.getString(0);
                        item.video_duration_ms = getfileduration(Uri.parse(whatsappcursor.getString(0)));
                        Log.d("resp", "" + item.video_duration_ms);
                        if (item.video_duration_ms > 5000) {
                            item.video_time = change_sec_to_time(item.video_duration_ms);
                            data_list.add(item);
                            progressDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyItemInserted(data_list.size());
                                }
                            });
                        }
                    }
                    whatsappcursor.close();
                }
                /*
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                 */
            }
        }).start();

    }

    // get the audio file duration that is store in our directory
    public long getfileduration(Uri uri) {
        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            return file_duration;
        } catch (Exception e) {

        }
        return 0;
    }


    public String change_sec_to_time(long file_duration) {
        long second = (file_duration / 1000) % 60;
        long minute = (file_duration / (1000 * 60)) % 60;

        return String.format("%02d:%02d", minute, second);

    }


    public void Chnage_Video_size(String src_path, String destination_path) {

        Functions.Show_determinent_loader(this, false, false);
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

                                Intent intent = new Intent(GalleryVideos_A.this, GallerySelectedVideo_A.class);
                                intent.putExtra("video_path", Variables.gallery_resize_video);
                                startActivity(intent);

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

                                    Toast.makeText(GalleryVideos_A.this, "Try Again", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {

                                }
                            }
                        });

                    }
                })
                .start();
    }

    private void checkToTrimorUseOriginal(GalleryVideo_Get_Set item) {
        /* commenting out the edit video option as we need to use sdk 29 and ffmpeg wont work*/
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Would you like to edit the video or use original ?")
                .setNegativeButton("Use Original", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (item.video_duration_ms < Variables.max_recording_duration) {
                            Chnage_Video_size(item.video_path, Variables.gallery_resize_video);
                        } else {
                            try {
                                startTrim(new File(item.video_path), new File(Variables.gallery_trimed_video), 1000, 18000);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .setPositiveButton("Edit the video", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivityForResult(VideoCropActivity.createIntent(GalleryVideos_A.this, item.video_path, Variables.app_folder + "/output_crop.mp4"), TrimmerConstants.REQ_CODE_VIDEO_TRIMMER);
                        //startActivityForResult(VideoTrimmerActivity.createIntent(GalleryVideos_A.this, item.video_path, Variables.app_folder + "/output_crop.mp4"), TrimmerConstants.REQ_CODE_VIDEO_TRIMMER);
                    }
                }).show();


        // temporary fix
        /*
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("You can post a video with the length of " + String.valueOf(Variables.max_recording_duration / 1000) + " seconds only. If the length of your video is more than that, then only initial " + String.valueOf(Variables.max_recording_duration / 1000) + " seconds will be posted. ")
                .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (item.video_duration_ms < Variables.max_recording_duration) {
                            Chnage_Video_size(item.video_path, Variables.gallery_resize_video);
                        } else {
                            try {
                                startTrim(new File(item.video_path), new File(Variables.gallery_trimed_video), 1000, 18000);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        // temporary fix

         */

    }

    public void startTrim(final File src, final File dst, final int startMs, final int endMs) throws IOException {

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
                    mvhd.setMatrix(Matrix.ROTATE_180);
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
                Functions.Show_indeterminent_loader(GalleryVideos_A.this, true, true);
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.equals("error")) {
                    Toast.makeText(GalleryVideos_A.this, "Try Again", Toast.LENGTH_SHORT).show();
                } else {
                    Functions.cancel_indeterminent_loader();
                    Chnage_Video_size(Variables.gallery_trimed_video, Variables.gallery_resize_video);
                }
            }


        }.execute();

    }


    @Override
    protected void onStart() {
        super.onStart();
        DeleteFile();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        DeleteFile();
    }

    public void DeleteFile() {
        File output = new File(Variables.outputfile);
        File output2 = new File(Variables.outputfile2);
        File output_filter_file = new File(Variables.output_filter_file);
        File gallery_trim_video = new File(Variables.gallery_trimed_video);
        File gallery_resize_video = new File(Variables.gallery_resize_video);

        if (output.exists()) {
            output.delete();
        }
        if (output2.exists()) {

            output2.delete();
        }
        if (output_filter_file.exists()) {
            output_filter_file.delete();
        }

        if (gallery_trim_video.exists()) {
            gallery_trim_video.delete();
        }

        if (gallery_resize_video.exists()) {
            gallery_resize_video.delete();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == TrimmerConstants.REQ_CODE_VIDEO_TRIMMER && data != null)
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent(GalleryVideos_A.this, GallerySelectedVideo_A.class);
            //intent.putExtra("video_path", Variables.gallery_resize_video);
            intent.putExtra("video_path", Variables.app_folder + "/output_crop.mp4");
            startActivity(intent);
        }
    }


}
