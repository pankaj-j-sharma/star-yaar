package com.incampusit.staryaar.videotrim.features.record;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.incampusit.staryaar.R;
import com.incampusit.staryaar.videotrim.features.common.ui.BaseActivity;
import com.incampusit.staryaar.videotrim.features.record.view.PreviewSurfaceView;

public class VideoRecordActivity extends BaseActivity implements View.OnClickListener {
    private PreviewSurfaceView mGLView;
    private ImageView mIvRecordBtn;
    private ImageView mIvSwitchCameraBtn;

    public static void call(Context context) {
        context.startActivity(new Intent(context, VideoRecordActivity.class));
    }

    @Override
    public void initUI() {
        setContentView(R.layout.activity_video_recording);
        //mGLView = this.findViewById(R.id.glView);
        mIvRecordBtn = this.findViewById(R.id.ivRecord);
        mIvSwitchCameraBtn = this.findViewById(R.id.ivSwitch);
        ImageView ivBack = this.findViewById(R.id.iv_back);
        mIvRecordBtn.setOnClickListener(this);
        mIvSwitchCameraBtn.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        mGLView.startPreview();
    }

    @Override
    public void onClick(View view) {
        if (R.id.ivRecord == view.getId()) {
            mGLView.startPreview();
        } else if (R.id.iv_back == view.getId()) {
            finish();
        }
    }
}
