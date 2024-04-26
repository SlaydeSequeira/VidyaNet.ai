package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import okhttp3.*;

import java.io.IOException;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NodeChecker extends AppCompatActivity {
    OkHttpClient client;
    private static final String TAG = "NodeChecker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_checker);

        // Initialize OkHttpClient
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        sendPostRequest();
    }

    private void sendPostRequest() {
        // Build the request body with JSON data
        String jsonBody = "{\"resume\": \"Your resume content here\"}"; // Replace with your resume content
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url("https://6daa-117-244-125-146.ngrok-free.app/get-suggestions") // Update with your Ngrok forwarding address
                .post(body)
                .build();

        // Make the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Response: " + responseBody);

                    // Process the response (consider using a JSON parser like Gson)
                } else {
                    Log.e(TAG, "Unsuccessful response: " + response.code());
                }
            }
        });
    }
}
