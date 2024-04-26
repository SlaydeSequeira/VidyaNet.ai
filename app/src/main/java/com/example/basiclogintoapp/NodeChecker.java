package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import okhttp3.*;

import java.io.IOException;

public class NodeChecker extends AppCompatActivity {

    private static final String TAG = "NodeChecker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_checker);

        // Create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Build the request body
        String jsonBody = "{\"resume\": \"Your resume content here\"}"; // Replace with your resume content

        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        // Build the request
        Request request = new Request.Builder()
                .url("http://192.168.84.113:3000/get-suggestions")
                .post(body)
                .build();

        // Make the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Get response body as string
                    String responseBody = response.body().string();
                    Log.d(TAG, "Response: " + responseBody);

                    // You can process the response here
                } else {
                    // Handle unsuccessful response
                    Log.e(TAG, "Unsuccessful response: " + response);
                }
            }
        });
    }
}
