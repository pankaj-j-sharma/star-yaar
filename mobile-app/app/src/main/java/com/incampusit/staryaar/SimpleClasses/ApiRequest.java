package com.incampusit.staryaar.SimpleClasses;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;


public class ApiRequest {

    private static final String DEFAULT_CACHE_DIR = "volley";

    public static void Call_Api(final Context context, String url, JSONObject jsonObject,
                                final Callback callback) {

        final String[] urlsplit = url.split("/");
        Log.d(Variables.tag, urlsplit[urlsplit.length - 1]);

        if (jsonObject != null)
            Log.d(Variables.tag + urlsplit[urlsplit.length - 1], jsonObject.toString());

        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        DiskBasedCache cache = new DiskBasedCache(cacheDir);
        cache.clear();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(Variables.tag + urlsplit[urlsplit.length - 1], response.toString());

                        if (callback != null)
                            callback.Responce(response.toString());

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(Variables.tag + urlsplit[urlsplit.length - 1], error.toString());
                //Toast.makeText(context, "Chat Api run timeout", Toast.LENGTH_SHORT).show();
                if (callback != null)
                    callback.Responce(error.toString());

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjReq);
    }
}
