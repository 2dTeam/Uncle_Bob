package org.team2d.uncle_bob.Network;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by nikolaev on 17.11.16.
 */

public class Network {
    //TODO Dagger2 DI
    private static OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String backendURL = "http://unclebob.ml/order";

    private static void makePostRequest(String url, RequestBody requestBody, Callback callback) throws IOException {
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void sendOrderToServer(JSONObject json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json.toString());
        try {
            makePostRequest(backendURL, body, callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
