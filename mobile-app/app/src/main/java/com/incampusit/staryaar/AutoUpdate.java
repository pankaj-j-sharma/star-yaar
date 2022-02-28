package com.incampusit.staryaar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.incampusit.staryaar.Main_Menu.MainMenuActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AutoUpdate extends AsyncTask<String, Integer, String> {
    private ProgressDialog mPDialog;
    private Context mContext;

    void setContext(Activity context) {
        mContext = context;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPDialog = new ProgressDialog(mContext);
                mPDialog.setMessage("Please wait...");
                mPDialog.setIndeterminate(true);
                mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mPDialog.setCancelable(false);
                mPDialog.show();
            }
        });
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {
            URL url = new URL(arg0[0]);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory() + "/download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "app.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();
            int lenghtOfFile = c.getContentLength();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            long total = 0;
            while ((len1 = is.read(buffer)) != -1) {
                total += len1;
                fos.write(buffer, 0, len1);
                publishProgress((int) ((total * 100) / lenghtOfFile));
            }
            fos.close();
            is.close();//till here, it works fine - .apk is download to my sdcard in download file

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri photoURI = FileProvider.getUriForFile(mContext,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    new File(Environment.getExternalStorageDirectory() + "/download/" + "app.apk"));

            intent.setDataAndType(photoURI, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            mContext.startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(mContext, "Update error! " + e.getStackTrace().toString(), Toast.LENGTH_LONG).show();
            Log.d("errrrr", e.getStackTrace().toString());
        } finally {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mPDialog != null)
            mPDialog.show();

    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (mPDialog != null) {
            mPDialog.setIndeterminate(false);
            mPDialog.setMax(100);
            mPDialog.setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (mPDialog != null)
            mPDialog.dismiss();
        if (result != null)
            Toast.makeText(mContext, "Download error: " + result, Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(mContext, "File Downloaded", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, MainMenuActivity.class);
            mContext.startActivity(intent);
        }
    }

}
