package com.incampusit.staryaar.argear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.incampusit.staryaar.R;


public class BulgeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = BulgeFragment.class.getSimpleName();
    View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bulge, container, false);

        rootView.findViewById(R.id.close_bulge_button).setOnClickListener(this);
        rootView.findViewById(R.id.clear_bulge_button).setOnClickListener(this);
        rootView.findViewById(R.id.bulge_fun1_button).setOnClickListener(this);
        rootView.findViewById(R.id.bulge_fun2_button).setOnClickListener(this);
        rootView.findViewById(R.id.bulge_fun3_button).setOnClickListener(this);
        rootView.findViewById(R.id.bulge_fun4_button).setOnClickListener(this);
        rootView.findViewById(R.id.bulge_fun5_button).setOnClickListener(this);
        rootView.findViewById(R.id.bulge_fun6_button).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        //v.setBackgroundColor(v.getContext().getColor(R.color.white));
        deselect_bulge_buttons(v);
        switch (v.getId()) {
            case R.id.close_bulge_button:
                getActivity().onBackPressed();
                break;
            case R.id.clear_bulge_button:
                ((ArGearCameraActivity) getActivity()).clearBulge();
                break;
            case R.id.bulge_fun1_button:
                v.setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background_selected));
                v.setPadding(20, 20, 20, 20);
                v.setElevation(20);
                ((ArGearCameraActivity) getActivity()).setBulgeFunType(1);
                break;
            case R.id.bulge_fun2_button:
                v.setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background_selected));
                v.setPadding(20, 20, 20, 20);
                v.setElevation(20);
                ((ArGearCameraActivity) getActivity()).setBulgeFunType(2);
                break;
            case R.id.bulge_fun3_button:
                v.setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background_selected));
                v.setPadding(20, 20, 20, 20);
                v.setElevation(20);
                ((ArGearCameraActivity) getActivity()).setBulgeFunType(3);
                break;
            case R.id.bulge_fun4_button:
                v.setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background_selected));
                v.setPadding(20, 20, 20, 20);
                v.setElevation(20);
                ((ArGearCameraActivity) getActivity()).setBulgeFunType(4);
                break;
            case R.id.bulge_fun5_button:
                v.setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background_selected));
                v.setPadding(20, 20, 20, 20);
                v.setElevation(20);
                ((ArGearCameraActivity) getActivity()).setBulgeFunType(5);
                break;
            case R.id.bulge_fun6_button:
                v.setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background_selected));
                v.setPadding(20, 20, 20, 20);
                v.setElevation(20);
                ((ArGearCameraActivity) getActivity()).setBulgeFunType(6);
                break;
        }
    }

    private void deselect_bulge_buttons(View v) {

        rootView.findViewById(R.id.bulge_fun1_button).setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background));
        rootView.findViewById(R.id.bulge_fun1_button).setPadding(2, 2, 2, 2);
        rootView.findViewById(R.id.bulge_fun1_button).setElevation(0);

        rootView.findViewById(R.id.bulge_fun2_button).setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background));
        rootView.findViewById(R.id.bulge_fun2_button).setPadding(2, 2, 2, 2);
        rootView.findViewById(R.id.bulge_fun2_button).setElevation(0);

        rootView.findViewById(R.id.bulge_fun3_button).setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background));
        rootView.findViewById(R.id.bulge_fun3_button).setPadding(2, 2, 2, 2);
        rootView.findViewById(R.id.bulge_fun3_button).setElevation(0);

        rootView.findViewById(R.id.bulge_fun4_button).setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background));
        rootView.findViewById(R.id.bulge_fun4_button).setPadding(2, 2, 2, 2);
        rootView.findViewById(R.id.bulge_fun4_button).setElevation(0);

        rootView.findViewById(R.id.bulge_fun5_button).setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background));
        rootView.findViewById(R.id.bulge_fun5_button).setPadding(2, 2, 2, 2);
        rootView.findViewById(R.id.bulge_fun5_button).setElevation(0);

        rootView.findViewById(R.id.bulge_fun6_button).setBackground(v.getContext().getDrawable(R.drawable.camera_filter_background));
        rootView.findViewById(R.id.bulge_fun6_button).setPadding(2, 2, 2, 2);
        rootView.findViewById(R.id.bulge_fun6_button).setElevation(0);
    }
}
