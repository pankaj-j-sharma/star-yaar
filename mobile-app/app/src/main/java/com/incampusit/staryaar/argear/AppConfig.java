package com.incampusit.staryaar.argear;


import com.incampusit.staryaar.BuildConfig;

public class AppConfig {

    public static final String API_URL = "https://apis.argear.io";
    public static final String API_KEY = "9dd38076802fce5bd7a959de";
    public static final String SECRET_KEY = "cf743062e4385c764f5c50ce7a878c110382741eaa543ae91af5d94a7c955f25";
    public static final String AUTH_KEY = "U2FsdGVkX1+PHjCfxTGv5vYciALeXNYrSPFKL1F98dQW26F6QA96aFso5/YtC1EC/DCvrnR22kwGdEZY6yASaQ==";

    public static final String APPLICATION_ID = "com.incampusit.staryaar";
    // preference
    public static final String USER_PREF_NAME = BuildConfig.APPLICATION_ID + ".Preference";
    public static final String USER_PREF_NAME_FILTER = BuildConfig.APPLICATION_ID + ".ARGearFilter.Preference";
    public static final String USER_PREF_NAME_STICKER = BuildConfig.APPLICATION_ID + ".ARGearItem.Preference";

    // camera
    // 1: CAMERA_API_1, 2: CAMERA_API_2
    public static final int USE_CAMERA_API = 2;

    // region - beauty sample
    public static final float[] BEAUTY_TYPE_INIT_VALUE = {
            10,     //VLINE
            90,     //ACE_SLIM
            55,     //JAW
            -50,    //CHIN
            5,      //EYE
            -10,    //EYE_GAP
            0,      //NOSE_LINE
            35,     //NOSE_SIDE
            30,     //NOSE_LENGTH
            -35,    //MOUTH_SIZE
            0,      //EYE_BACK
            0,      //EYE_CORNER
            0,      //LIP_SIZE
            50,     //SKIN
            0,      //DARK_CIRCLE
            0,      //MOUTH_WRINKLE
    };

    public static final float[] BASIC_BEAUTY_1 = {
            20, 10, 45, 45, 5, -10, 40, 20, 15, 0, 0, 0, 0, 50, 0, 0
    };

    public static final float[] BASIC_BEAUTY_2 = {
            10, 90, 55, -50, 5, -10, 0, 35, 30, -35, 0, 0, 0, 50, 0, 0
    };

    public static final float[] BASIC_BEAUTY_3 = {
            25, 20, 50, -25, 25, -10, 30, 40, 90, 0, 0, 0, 0, 50, 0, 0
    };
    // endregion

}
