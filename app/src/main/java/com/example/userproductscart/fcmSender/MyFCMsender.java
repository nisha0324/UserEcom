package com.example.userproductscart.fcmSender;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MyFCMsender {
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send"
            ,KEY_STRING = "key = AAAAi9ZR89A:APA91bHpjqqYd9ZPLQYRq1kgjKXdyQCY8go2eNUnMet3aXb1giCUpxoDgn-z4CHTzidWDm0rI3DUF7VhnkcTBpO9aHEgs0ZBwL3UpJmrZvfEXiOlh5IaXsaIs7oY7k9yPTgGyTmrfhq-";

    OkHttpClient client = new OkHttpClient();

    public void send(String message, Callback callback){

        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(FCM_URL)
                .addHeader("Content-type", "application/json")
                .addHeader("Authorization", KEY_STRING)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
