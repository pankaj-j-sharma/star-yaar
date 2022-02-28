package com.incampusit.staryaar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.incampusit.staryaar.Main_Menu.MainMenuActivity;
import com.incampusit.staryaar.SimpleClasses.Variables;

import java.io.File;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Splash_A extends AppCompatActivity {


    private static final int PERMISSION_REQUEST_CODE = 200;

    public AppCompatActivity activity = this;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        // look for data in the recieved notifications
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(Variables.TAG, "Notification click ");

            for (String key : bundle.keySet()) {
                Log.e(Variables.TAG, key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
            }
        }

        //deeplinkedCall();

        Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);

        countDownTimer = new CountDownTimer(2000, 500) {

            public void onTick(long millisUntilFinished) {
                //checkRootDirPresent();
                /*
                FirebaseDatabase.getInstance().getReference().child("updateavailable").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean update = (boolean) snapshot.getValue();
                        if (update) {
                            FirebaseDatabase.getInstance().getReference().child("updateavailable").setValue(false);
                            autoUpdate();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                 */
            }

            public void onFinish() {
                callMainActivity();
            }
        }.start();
    }

    private void deeplinkedCall() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            Log.d(Variables.TAG, "Deep link launched " + deepLink.getQueryParameterNames() + deepLink.getEncodedPath());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "getDynamicLink:onFailure", e);
                    }
                });
    }


    private void callMainActivity() {

        Intent intent = new Intent(Splash_A.this, MainMenuActivity.class);
        if (getIntent().getExtras() != null) {
            intent.putExtras(getIntent().getExtras());
            setIntent(null);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }

    private void autoUpdate() {
        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (locationAccepted && cameraAccepted) {
                    AutoUpdate updateApp = new AutoUpdate();
                    updateApp.setContext(Splash_A.this);
                    updateApp.execute("http://incampus.co.in/api/app-debug.apk");
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void checkRootDirPresent() {
        File app_folder = new File(Variables.app_folder);
        if (!app_folder.exists() && !app_folder.isDirectory()) {
            app_folder.mkdir();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
