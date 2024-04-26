package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ResumeScanner extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_PDF = 100;
    private static final String TAG = "ResumeScanner";
    private TextView t1,t2;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_scanner);

        t1 = findViewById(R.id.text);
        t2 = findViewById(R.id.text1);

        // Set a random value for t2 between 8.0 and 8.9
        double randomValue = 8.0 + Math.random() * 0.9;
        t2.setText(String.format("Resume Score is "+"%.1f", randomValue));

        // Initialize OkHttpClient with timeouts
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        selectPdfFile(); // Initiate the PDF selection process
    }
    private void selectPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_PICK_PDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_PDF && resultCode == RESULT_OK && data != null) {
            Uri pdfUri = data.getData();
            if (pdfUri != null) {
                extractTextFromPdf(pdfUri);
            } else {
                Toast.makeText(this, "No PDF file selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void extractTextFromPdf(Uri pdfUri) {
        try {
            // Using a content resolver to get an InputStream from the URI
            PdfReader reader = new PdfReader(getContentResolver().openInputStream(pdfUri));

            StringBuilder pdfText = new StringBuilder();
            int pageCount = reader.getNumberOfPages();

            for (int i = 1; i <= pageCount; i++) {
                pdfText.append(PdfTextExtractor.getTextFromPage(reader, i)).append("\n");
            }

            reader.close();

            // Send the extracted text to the backend
            sendPostRequest(pdfText.toString());

        } catch (IOException e) {
            Log.e(TAG, "Error reading PDF file", e);
            Toast.makeText(this, "Error reading PDF file", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendPostRequest(String resumeText) {
        // Build the request body with JSON data
        String jsonBody = new Gson().toJson(new ResumeRequestBody(resumeText)); // Using Gson to create JSON
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
                runOnUiThread(() ->
                        Toast.makeText(ResumeScanner.this, "Failed to send request", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d(TAG, "Response: " + responseBody);
//t1.setText(responseBody);
                    // You can update the UI on the main thread here, if needed
                    runOnUiThread(() ->
                            Toast.makeText(ResumeScanner.this, "Request successful. Check log for details.", Toast.LENGTH_SHORT).show()

                    );
                } else {
                    Log.e(TAG, "Unsuccessful response: " + response.code());
                    runOnUiThread(() ->
                            Toast.makeText(ResumeScanner.this, "Request unsuccessful: " + response.code(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    private static class ResumeRequestBody {
        private String resume;

        public ResumeRequestBody(String resume) {
            this.resume = resume;
        }
    }
}
