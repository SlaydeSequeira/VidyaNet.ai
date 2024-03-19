package com.example.basiclogintoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class UpdateReel extends AppCompatActivity {

    private static final int REQUEST_CODE_VIDEO_CAPTURE = 101;
    private static final int REQUEST_CODE_PERMISSIONS = 102;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reel);

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Check and request permissions
        if (!checkPermissions()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        } else {
            startRecording();
        }
    }

    private boolean checkPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startRecording() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_VIDEO_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            // Prompt user for title
            promptForTitle(videoUri);
        }
    }

    private void promptForTitle(final Uri videoUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Title");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = input.getText().toString();
                uploadVideoToFirebase(videoUri, title);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void uploadVideoToFirebase(final Uri videoUri, final String title) {
        if (videoUri != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            final StorageReference videoRef = storageRef.child("videos/" + timeStamp + ".mp4");

            videoRef.putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // URL of the uploaded video
                                    String downloadUrl = uri.toString();
                                    pushDataToFirebase(downloadUrl, title);
                                    showToast("Video uploaded successfully. URL: " + downloadUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to upload video: " + e.getMessage());
                        }
                    });
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            boolean allGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                startRecording();
            } else {
                showToast("Permissions required to record video.");
            }
        }
    }

    public void pushDataToFirebase(String urlString, String title) {
        // Get a reference to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("videos").push();

        // Push the URL and title to the database
        reference.child("url").setValue(urlString);
        reference.child("title").setValue(title);
    }
}
