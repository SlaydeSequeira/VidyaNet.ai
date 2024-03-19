package com.example.basiclogintoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnectPpl extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int IMAGE_CAPTURE_REQUEST_CODE = 101;
    EditText t1;

    private ImageView imageView;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_ppl);
        t1 = findViewById(R.id.text);
        imageView = findViewById(R.id.imageView);
        btn = findViewById(R.id.captureButton);
        // Request camera permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Camera permission already granted
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted
                dispatchTakePictureIntent();
            } else {
                // Camera permission denied
                Toast.makeText(this, "Camera permission required for OCR", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            extractTextFromImage(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE);
        }

    }

    private void extractTextFromImage(Bitmap bitmap) {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            // Handle error
            Toast.makeText(this, "Text recognizer could not be set up on your device", Toast.LENGTH_SHORT).show();
            return;
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);
        StringBuilder stringBuilder = new StringBuilder();

        for (int index = 0; index < textBlocks.size(); index++) {
            TextBlock textBlock = textBlocks.valueAt(index);
            stringBuilder.append(textBlock.getValue());
            stringBuilder.append("\n");
        }

        String extractedText = stringBuilder.toString();
        t1.setText(extractedText);
        // Do something with the extracted text, for example:
        Toast.makeText(this, "Extracted Text:\n" + extractedText, Toast.LENGTH_LONG).show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the extracted text from the EditText
                String extractedText = t1.getText().toString();

                // Split the text into an array of strings
                String[] textArray = extractedText.split("\n");
                FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

                // Get reference to your Firebase database
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("MyUsers").child(fuser.getUid()).child("toDo");

                // Push each string separately to the database
                for (String text : textArray) {
                    String key = databaseReference.push().getKey(); // Generate a unique key
                    databaseReference.child(key).setValue(text); // Set the value of the key to the text string
                }

                // Optionally, you can add a listener to know when the data is successfully written
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Data successfully written to the database
                        Log.d("Firebase", "Data successfully written to the database");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Data writing operation cancelled
                        Log.d("Firebase", "Data writing cancelled: " + error.getMessage());
                    }
                });
            }
        });


    }
}
