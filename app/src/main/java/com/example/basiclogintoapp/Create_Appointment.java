package com.example.basiclogintoapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;

public class Create_Appointment extends AppCompatActivity {

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent";
    private static final String API_KEY = "AIzaSyA07tAQqohdl3QUkCqPcLqRJ6YTGoG1rA0"; // Replace with your actual API key
    private final OkHttpClient client = new OkHttpClient();

    private EditText etName, etAge, etHeight, etWeight, etSymptoms, etMedicalHistory;
    private CheckBox cbConfirmSymptoms;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request feature **before** setting content view
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_appointment);

        getSupportActionBar().hide();

        // Initialize UI components
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);
        etSymptoms = findViewById(R.id.et_symptoms);
        etMedicalHistory = findViewById(R.id.et_medical_history);
        cbConfirmSymptoms = findViewById(R.id.cb_confirm_symptoms);
        btnSubmit = findViewById(R.id.btn_submit);

        // Set button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
    }


    private void submitForm() {
        String name = etName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String height = etHeight.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();
        String symptoms = etSymptoms.getText().toString().trim();
        String medicalHistory = etMedicalHistory.getText().toString().trim();
        boolean isConfirmed = cbConfirmSymptoms.isChecked();

        if (name.isEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty() || symptoms.isEmpty() || medicalHistory.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isConfirmed) {
            Toast.makeText(this, "Please confirm your symptoms!", Toast.LENGTH_SHORT).show();
            return;
        }

        generateMedicalReport(name, age, height, weight, symptoms, medicalHistory);
    }

    private void generateMedicalReport(String name, String age, String height, String weight,
                                       String symptoms, String medicalHistory) {
        // Create the prompt
        String prompt = String.format(
                "My name is %s, I am %s years old. My height is %s and weight is %s. " +
                        "I am experiencing the following symptoms: %s. " +
                        "My medical history includes: %s. " +
                        "Please generate a detailed medical report for my doctor to review.",
                name, age, height, weight, symptoms, medicalHistory
        );

        try {
            // Create request body with correct JSON structure
            JSONObject jsonBody = new JSONObject();

            // Create contents array with one content object
            JSONArray contentsArray = new JSONArray();
            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();
            JSONObject part = new JSONObject();
            part.put("text", prompt);
            parts.put(part);
            content.put("parts", parts);
            contentsArray.put(content);
            jsonBody.put("contents", contentsArray);

            // Add generation config
            JSONObject generationConfig = new JSONObject();
            generationConfig.put("temperature", 0.7);
            generationConfig.put("topK", 40);
            generationConfig.put("topP", 0.95);
            generationConfig.put("maxOutputTokens", 1024);
            jsonBody.put("generationConfig", generationConfig);

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    jsonBody.toString()
            );

            // Build request
            Request request = new Request.Builder()
                    .url(GEMINI_API_URL + "?key=" + API_KEY)
                    .post(body)
                    .build();

            // Make async request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(Create_Appointment.this,
                                "Error generating report: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();

                    runOnUiThread(() -> {
                        try {
                            // Parse JSON
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            JSONArray candidates = jsonResponse.getJSONArray("candidates");
                            if (candidates.length() > 0) {
                                JSONObject firstCandidate = candidates.getJSONObject(0);
                                JSONObject content = firstCandidate.getJSONObject("content");
                                JSONArray parts = content.getJSONArray("parts");

                                if (parts.length() > 0) {
                                    String reportText = parts.getJSONObject(0).getString("text"); // Extract the text

                                    // Firebase Database Reference
                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Report");

                                    // Generate a unique key
                                    String reportKey = databaseRef.push().getKey();

                                    if (reportKey != null) {
                                        // Push only the extracted text under the generated key
                                        databaseRef.setValue(reportText)
                                                .addOnSuccessListener(aVoid ->
                                                        Log.d("Firebase", "Report saved successfully!"))
                                                .addOnFailureListener(e ->
                                                        Log.e("Firebase", "Error saving report", e));
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("JSON", "Error parsing JSON", e);
                        }
                    });
                }


            });
        } catch (Exception e) {
            Toast.makeText(this, "Error creating request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}