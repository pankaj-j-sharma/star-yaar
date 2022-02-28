package com.incampusit.staryaar.Main_Menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.incampusit.staryaar.R;
import com.incampusit.staryaar.SimpleClasses.Functions;
import com.incampusit.staryaar.SimpleClasses.Variables;

import java.io.File;


public class MainMenuActivity extends AppCompatActivity {
    private static final int RC_APP_UPDATE = 11;
    public static MainMenuActivity mainMenuActivity;
    public static String token;
    public static Intent intent;
    long mBackPressed;
    private MainMenuFragment mainMenuFragment;
    private AppUpdateManager mAppUpdateManager;
    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADING) {
                        long bytesDownloaded = state.bytesDownloaded();
                        long totalBytesToDownload = state.totalBytesToDownload();
                        Log.d(Variables.TAG, "Downloading " + bytesDownloaded + "/" + totalBytesToDownload);
                        // Implement progress bar.
                    } else if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        Log.d(Variables.TAG, "App downloaded");
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        Log.d(Variables.TAG, "App installed");
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.i(Variables.TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };
    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            //Toast.makeText(context, "Main Activity "+message, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
        Functions.clearRootDirContents(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                onNotice, new IntentFilter("custom-event-name"));

        setContentView(R.layout.activity_main_menu);

        mainMenuActivity = this;

        intent = getIntent();

        setIntent(null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Variables.screen_height = displayMetrics.heightPixels;
        Variables.screen_width = displayMetrics.widthPixels;

        Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);

        Variables.user_id = Variables.sharedPreferences.getString(Variables.u_id, "");
        Variables.user_name = Variables.sharedPreferences.getString(Variables.u_name, "");
        Variables.user_pic = Variables.sharedPreferences.getString(Variables.u_pic, "");


        token = FirebaseInstanceId.getInstance().getToken();
        if (token == null || (token.equals("") || token.equals("null")))
            token = Variables.sharedPreferences.getString(Variables.device_token, "null");


        if (savedInstanceState == null) {

            initScreen();

        } else {
            mainMenuFragment = (MainMenuFragment) getSupportFragmentManager().getFragments().get(0);
        }


    }


    private void initScreen() {

        mainMenuFragment = new MainMenuFragment();
        // pass the extras recieved if any
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mainMenuFragment.setArguments(bundle);
        }
        // pass the extras recieved if any
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, mainMenuFragment)
                .commit();

        findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // create directory
        createDir();
    }

    public void createDir() {

        //File folder = new File(Environment.getExternalStorageDirectory() +
        //        File.separator + "StarYaarMedia");
        File folder = new File(Variables.app_folder);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            //Toast.makeText(this, "StarYaarMedia folder is present ", Toast.LENGTH_SHORT).show();
        } else {
            // Do something else on failure
        }
    }

    @Override
    public void onBackPressed() {
        if (!mainMenuFragment.onBackPressed()) {
            int count = this.getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                if (mBackPressed + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    return;
                } else {
                    Toast.makeText(getBaseContext(), "Tap Again To Exit", Toast.LENGTH_SHORT).show();
                    mBackPressed = System.currentTimeMillis();

                }
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                onNotice, new IntentFilter("custom-event-name"));
        AppUpdateCheck(this);
    }

    public void AppUpdateCheck(Context context) {
        DatabaseReference root_ref;
        ValueEventListener eventListener2;
        Query inbox_query;

        root_ref = FirebaseDatabase.getInstance().getReference();
        root_ref.child("UpdateAvailable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    try {
                        if (!(boolean) ds.getValue())
                            return;
                        Log.d(Variables.TAG, "App Update launched");
                        mAppUpdateManager = AppUpdateManagerFactory.create(context);
                        mAppUpdateManager.registerListener(installStateUpdatedListener);
                        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                            Log.d(Variables.TAG, "AvailableVersion " + appUpdateInfo.availableVersionCode() + " " + appUpdateInfo.updateAvailability());
                            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)) {
                                try {
                                    mAppUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo, AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/, MainMenuActivity.this, RC_APP_UPDATE);

                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }

                            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                                popupSnackbarForCompleteUpdate();
                            } else {
                                Log.e(Variables.TAG, "checkForAppUpdateAvailability: something else");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e(Variables.TAG, "onActivityResult: app download failed");
            } else {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            }
        }
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.container),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.app_color_light));
        snackbar.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

}
