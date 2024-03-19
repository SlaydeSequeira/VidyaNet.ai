package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewItemPrediction extends AppCompatActivity {
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;

    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_prediction);
        editText1 = findViewById(R.id.edittext1);
        editText2 = findViewById(R.id.edittext2);
        editText3 = findViewById(R.id.edittext3);
        editText4 = findViewById(R.id.edittext4);
        editText5 = findViewById(R.id.edittext5);
        t1 = findViewById(R.id.text);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Get the values from EditText widgets
                    String value2 = editText2.getText().toString();
                    double value3 = Double.parseDouble(editText3.getText().toString());
                    double value4 = Double.parseDouble(editText4.getText().toString());
                    double value5 = Double.parseDouble(editText5.getText().toString());

                    // Use Uri.Builder to construct the URL
                    Uri.Builder builder = Uri.parse("https://bc18-103-116-169-162.ngrok-free.app/predict").buildUpon();
                    builder.appendQueryParameter("category", value2);
                    builder.appendQueryParameter("age", String.valueOf(value3));
                    builder.appendQueryParameter("weight", String.valueOf(value4));
                    builder.appendQueryParameter("size", String.valueOf(value5));

                    // Build the final URL
                    String finalUrl = builder.build().toString();

                    // Example: Make a GET request with parameters
                    new HttpRequestTask().execute(finalUrl);
                } catch (NumberFormatException e) {
                    // Handle the case where the user entered invalid values
                    Toast.makeText(NewItemPrediction.this, "Invalid input. Please enter valid decimal numbers.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public class HttpRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;

            try {
                // Create a URL object with the Flask server endpoint
                URL url = new URL(params[0]);

                // Open a connection to the URL
                connection = (HttpURLConnection) url.openConnection();

                // Set the request method (GET, POST, etc.)
                connection.setRequestMethod("GET");

                // Get the response code
                int responseCode = connection.getResponseCode();
                Log.d("HTTP", "Response Code: " + responseCode);

                // Read the response from the server
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Return the server response
                    return response.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                // Ensure that the connection is closed
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(result);

                    // Extract the value you want
                    int predictedSales = jsonResponse.getInt("predicted_time_left");

                    // Display the extracted value in your TextView
                    t1.setText("The predicted time left before expiry will be " + predictedSales+" days");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSON", "Error parsing JSON response");
                }
            } else {
                Log.e("HTTP", "Error making request to the server");
            }
        }

    }
}