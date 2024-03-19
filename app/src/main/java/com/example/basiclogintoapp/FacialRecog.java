package com.example.basiclogintoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import java.util.List;
public class FacialRecog extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private ImageView imageView;
    private FaceDetector faceDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facial_recog);

        imageView = findViewById(R.id.imageView);

        // Configure face detector options
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        // Get an instance of FaceDetector
        faceDetector = FaceDetection.getClient(options);

        // Request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Open the camera when the activity starts (you may want to trigger this with a button)
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                dispatchTakePictureIntent();
            } else {
                // Permission denied, handle accordingly (e.g., show a message, disable camera functionality)
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);

            // Process the image with face detection
            detectFaces(bitmap);
        }
    }

    private void detectFaces(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        faceDetector.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        // Handle successful face detection
                        for (Face face : faces) {
                            // Access face information (bounding box, landmarks, etc.)
                            // You can also label faces with names here based on your requirement
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle face detection failure
                        Toast.makeText(FacialRecog.this, "Face detection failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
