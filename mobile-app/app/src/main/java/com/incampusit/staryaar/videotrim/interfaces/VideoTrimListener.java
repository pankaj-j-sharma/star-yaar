package com.incampusit.staryaar.videotrim.interfaces;

public interface VideoTrimListener {
    void onStartTrim();

    void onFinishTrim(String url);

    void onCancel();
}
