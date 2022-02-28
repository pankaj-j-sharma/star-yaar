package com.incampusit.staryaar.videotrim.features.select;

import android.Manifest;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.incampusit.staryaar.R;
import com.incampusit.staryaar.databinding.ActivityVideoSelectBinding;
import com.incampusit.staryaar.videotrim.features.common.ui.BaseActivity;
import com.incampusit.staryaar.videotrim.features.record.view.PreviewSurfaceView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import iknow.android.utils.callback.SimpleCallback;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class VideoSelectActivity extends BaseActivity implements View.OnClickListener {

    private ActivityVideoSelectBinding mBinding;
    private VideoSelectAdapter mVideoSelectAdapter;
    private VideoLoadManager mVideoLoadManager;
    private PreviewSurfaceView mSurfaceView;
    private ViewGroup mCameraSurfaceViewLy;

    @SuppressLint("CheckResult")
    @Override
    public void initUI() {
        mVideoLoadManager = new VideoLoadManager();
        mVideoLoadManager.setLoader(new VideoCursorLoader());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_select);
        mCameraSurfaceViewLy = findViewById(R.id.layout_surface_view);
        mBinding.mBtnBack.setOnClickListener(this);
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) { // Always true pre-M
                mVideoLoadManager.load(this, new SimpleCallback() {
                    @Override
                    public void success(Object obj) {
                        if (mVideoSelectAdapter == null) {
                            mVideoSelectAdapter = new VideoSelectAdapter(VideoSelectActivity.this, (Cursor) obj);
                        } else {
                            mVideoSelectAdapter.swapCursor((Cursor) obj);
                        }
                        if (mBinding.videoGridview.getAdapter() == null) {
                            mBinding.videoGridview.setAdapter(mVideoSelectAdapter);
                        }
                        mVideoSelectAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                finish();
            }
        });
        if (rxPermissions.isGranted(Manifest.permission.CAMERA)) {
            initCameraPreview();
        } else {
            mBinding.cameraPreviewLy.setVisibility(View.GONE);
            mBinding.openCameraPermissionLy.setVisibility(View.VISIBLE);
            mBinding.mOpenCameraPermission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rxPermissions.request(Manifest.permission.CAMERA).subscribe(granted -> {
                        if (granted) {
                            initCameraPreview();
                        }
                    });
                }
            });
        }
    }

    private void initCameraPreview() {
        mSurfaceView = new PreviewSurfaceView(this);
        mBinding.cameraPreviewLy.setVisibility(View.VISIBLE);
        mBinding.openCameraPermissionLy.setVisibility(View.GONE);
        addSurfaceView(mSurfaceView);
        mSurfaceView.startPreview();

        mBinding.cameraPreviewLy.setOnClickListener(v -> {
            //hideOtherView();
            onBackPressed();
            mBinding.realCameraPreviewLy.findViewById(R.id.iv_back).setOnClickListener(view -> {
                //resetHideOtherView();
                onBackPressed();
            });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);
        finish();
    }

    private void hideOtherView() {
        mBinding.titleLayout.setVisibility(View.GONE);
        mBinding.videoGridview.setVisibility(View.GONE);
        mBinding.cameraPreviewLy.setVisibility(View.GONE);
    }

    private void resetHideOtherView() {
        mBinding.titleLayout.setVisibility(View.VISIBLE);
        mBinding.videoGridview.setVisibility(View.VISIBLE);
        mBinding.cameraPreviewLy.setVisibility(View.VISIBLE);
    }

    private void addSurfaceView(PreviewSurfaceView surfaceView) {
        mCameraSurfaceViewLy.addView(surfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBinding.mBtnBack.getId()) {
            finish();
        }
    }
}
