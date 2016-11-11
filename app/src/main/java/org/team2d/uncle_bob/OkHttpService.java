package org.team2d.uncle_bob;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;



import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by pavelrukavishnikov on 11.11.16.
 */

public class OkHttpService extends IntentService {
    OkHttpClient client = new OkHttpClient();;
    Request request = null;

    public OkHttpService() {
        super("OkHttpService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //Example implementation
        String url = new String("");

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);


        Request.Builder requestBuilder = new Request.Builder().url(url);

        //check data and call post

    }
}
