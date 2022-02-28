package com.incampusit.staryaar.videotrim.features.select;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;
import com.incampusit.staryaar.videotrim.features.trim.VideoTrimmerActivity;

import java.io.File;

import iknow.android.utils.DateUtil;
import iknow.android.utils.DeviceUtil;

public class VideoSelectAdapter extends CursorAdapter {

    private int videoCoverSize = DeviceUtil.getDeviceWidth() / 3;
    private MediaMetadataRetriever mMetadataRetriever;
    private Context mContext;

    VideoSelectAdapter(Context context, Cursor c) {
        super(context, c);
        this.mContext = context;
        mMetadataRetriever = new MediaMetadataRetriever();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.video_select_gridview_item, null);
        VideoGridViewHolder holder = new VideoGridViewHolder();
        holder.videoItemView = itemView.findViewById(R.id.video_view);
        holder.videoCover = itemView.findViewById(R.id.cover_image);
        holder.durationTv = itemView.findViewById(R.id.video_duration);
        itemView.setTag(holder);
        return itemView;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        final VideoGridViewHolder holder = (VideoGridViewHolder) view.getTag();
        final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        if (!checkDataValid(cursor)) {
            return;
        }
        final String duration = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        holder.durationTv.setText(DateUtil.convertSecondsToTime(Integer.parseInt(duration) / 1000));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.videoCover.getLayoutParams();
        params.width = videoCoverSize;
        params.height = videoCoverSize;
        holder.videoCover.setLayoutParams(params);
        Glide.with(context)
                .load(getVideoUri(cursor))
                .centerCrop()
                .override(videoCoverSize, videoCoverSize)
                .into(holder.videoCover);
        holder.videoItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOriginalOrEditVideo(mContext, path, duration);
            }
        });
    }

    private void confirmOriginalOrEditVideo(Context context, String videopath, String duration) {
        new AlertDialog.Builder(context)
                .setTitle("Confirm")
                .setMessage("Would you like to edit the video or use original ?")
                .setNegativeButton("Use Original", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (Integer.parseInt(duration) < Variables.max_recording_duration) {
                            Functions.Chnage_Video_size(mContext, videopath, Variables.gallery_resize_video);
                        } else {
                            try {
                                Functions.startTrim(mContext, new File(videopath), new File(Variables.gallery_trimed_video), 1000, 18000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .setPositiveButton("Edit the video", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        VideoTrimmerActivity.call((FragmentActivity) mContext, videopath);
                    }
                }).show();

    }

    private boolean checkDataValid(final Cursor cursor) {
        final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
            return false;
        }
        try {
            mMetadataRetriever.setDataSource(path);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        final String duration = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return !TextUtils.isEmpty(duration);
    }

    private Uri getVideoUri(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
        return Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
    }

    private static class VideoGridViewHolder {
        ImageView videoCover;
        View videoItemView;
        TextView durationTv;
    }
}
