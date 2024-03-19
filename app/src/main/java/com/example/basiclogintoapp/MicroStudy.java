
package com.example.basiclogintoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class MicroStudy extends AppCompatActivity {

    private VideoView videoView;
    private TextView timerTextView;
    private Handler handler;
    private Runnable runnable;
    private long elapsedTimeInMillis = 0;
    private boolean isTimerPaused = false;
    private static final String ELAPSED_TIME_KEY = "elapsed_time";
    int cardNumber;
    String cardNumberString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_study);
        WebView webView = findViewById(R.id.webview);
        String video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/kqtD5dpn9C8?si=ABu-lXaA_39mfnHY\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        Intent intent = getIntent();
        cardNumber = intent.getIntExtra("cardNumber", -1); // -1 is the default value if "cardNumber" extra is not found
        if (cardNumber != -1) {
            // The cardNumber is successfully retrieved
            cardNumberString = String.valueOf(cardNumber);
            // You can use cardNumberString wherever you need it
        }
        if(cardNumber==2)
        {
            video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/UmnCZ7-9yDY?si=vFue0Vf3zb-3-JVj\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        }
        if(cardNumber==3)
        {
            video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/EExSSotojVI?si=bY7Pq0qBlj8j0ZCn\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        }
        webView.loadData(video,"text/html","utf-8");


// Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());
        // Find the VideoView and TextView in the layout
        videoView = findViewById(R.id.videoView);
        timerTextView = findViewById(R.id.timerTextView);



// Start the video
        videoView.start();

        // Set the video URI and start playing
        String videoUrl = "https://firebasestorage.googleapis.com/v0/b/basiclogintoapp.appspot.com/o/videos%2F20240316_205549.mp4?alt=media&token=b062b9a4-db26-494f-910f-747a06a70859"; // Replace with your direct video URL from Google Drive
        Uri uri = Uri.parse(videoUrl);
       videoView.setVideoURI(uri);
        // Add media controller to enable player controls like play, pause, etc.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("MyUsers").child(fuser.getUid()).child("points");
       DatabaseReference dr = firebaseDatabase.getReference("MyUsers").child(fuser.getUid()).child("course").child(cardNumberString);

        showNotification("Your Study Session has started");
        // Start the video
        videoView.start();

        // Initialize handler and runnable to update the timer
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!isTimerPaused) {
                    elapsedTimeInMillis += 1000;
                    updateTimerText();
                }
                handler.postDelayed(this, 1000); // Update timer every second
                if (elapsedTimeInMillis == 10000) {
                    dr.setValue(1);
                    Toast.makeText(MicroStudy.this, "Points Updated", Toast.LENGTH_SHORT).show();
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String valueString = snapshot.getValue(String.class);
                                if (valueString != null) {
                                    try {
                                        int value = Integer.parseInt(valueString);
                                        value++; // Increment the value by 1
                                        String updatedValueString = String.valueOf(value);
                                        databaseReference.setValue(updatedValueString); // Update the value in the database
                                    } catch (NumberFormatException e) {
                                        // Handle if the stored value is not a valid integer
                                        Log.e("ValueError", "Stored value is not a valid integer: " + valueString);
                                    }
                                }
                            }
                            // Remove the ValueEventListener after updating points
                            databaseReference.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled event
                        }
                    };

                    // Add the ValueEventListener
                    databaseReference.addListenerForSingleValueEvent(valueEventListener);
                }


            }
        };
        handler.postDelayed(runnable, 1000); // Start the timer
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showNotification("Hey Come Back and complete your Study Session");
        // Stop the timer when the activity is destroyed
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause the timer when the activity is paused
        isTimerPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume the timer when the activity is resumed
        isTimerPaused = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the elapsed time when the activity is paused
        outState.putLong(ELAPSED_TIME_KEY, elapsedTimeInMillis);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the elapsed time when the activity is resumed
        elapsedTimeInMillis = savedInstanceState.getLong(ELAPSED_TIME_KEY);
        updateTimerText();
    }

    private void updateTimerText() {
        int seconds = (int) (elapsedTimeInMillis / 1000) % 60;
        int minutes = (int) ((elapsedTimeInMillis / (1000 * 60)) % 60);
        int hours = (int) ((elapsedTimeInMillis / (1000 * 60 * 60)) % 24);

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeFormatted);
    }
    private void showNotification(String message) {
        // Create a notification channel
        String channelId = "channel_id";
        CharSequence channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }

        // Create an intent to open the app when the notification is clicked
        Intent intent = new Intent(this, MicroStudy.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Create the notification
        Notification notification = new Notification.Builder(this, channelId)
                .setContentTitle("Study Reminder")
                .setContentText(message)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Show the notification
        if (notificationManager != null) {
            notificationManager.notify(0, notification);
        }
    }

}
