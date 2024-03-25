
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
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.MediaController;
import android.widget.RelativeLayout;
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
    TextView t1,t2,t3,t4;
    RelativeLayout r1,r2,r3,r4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_study);
        WebView webView = findViewById(R.id.webview);
        int cb1 = 0,cb2=0,cb3=0,cb4 = 0;
        CheckBox checkbox1 = findViewById(R.id.checkbox1);
        CheckBox checkbox2 = findViewById(R.id.checkbox2);
        CheckBox checkbox3 = findViewById(R.id.checkbox3);
        CheckBox checkbox4 = findViewById(R.id.checkbox4);
        r1 = findViewById(R.id.rell1);
        r2 = findViewById(R.id.rell2);
        r3 = findViewById(R.id.rell3);
        r4 = findViewById(R.id.rell4);

        t1 = findViewById(R.id.text1);
        t2 = findViewById(R.id.text2);
        t3 = findViewById(R.id.text3);
        t4 = findViewById(R.id.text4);


        String video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/kqtD5dpn9C8?si=ABu-lXaA_39mfnHY\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        Intent intent = getIntent();
        cardNumber = intent.getIntExtra("cardNumber", -1); // -1 is the default value if "cardNumber" extra is not found
        if (cardNumber != -1) {
            // The cardNumber is successfully retrieved
            cardNumberString = String.valueOf(cardNumber);
            t1.setText("Introduction to Python");
            t2.setText("Python Primer: Getting Started");
            t3.setText("Data Structures in Python");
            t4.setText("Advanced Python");
            // You can use cardNumberString wherever you need it
        }
        if(cardNumber==2)
        {
            t1.setText("Introduction to Java");
            t2.setText("Java Primer: Getting Started");
            t3.setText("Data Structures in Java");
            t4.setText("Advanced Java");
            video="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/UmnCZ7-9yDY?si=vFue0Vf3zb-3-JVj\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        }
        if(cardNumber==3)
        {
            t1.setText("Introduction to Kotlin");
            t2.setText("Kotlin Primer: Getting Started");
            t3.setText("Data Structures in Kotlin");
            t4.setText("Advanced Kotlin");

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
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String a = String.valueOf(snapshot.child("0").getValue());
                if (a.equals("1")) {
                    checkbox1.setChecked(true); // Set the checkbox checked if condition is met
                }
                String b = String.valueOf(snapshot.child("1").getValue());
                if (b.equals("1")) {
                    checkbox2.setChecked(true); // Set the checkbox checked if condition is met
                }
                String c = String.valueOf(snapshot.child("2").getValue());
                if (c.equals("1")) {
                    checkbox3.setChecked(true); // Set the checkbox checked if condition is met
                }
                String d = String.valueOf(snapshot.child("3").getValue());
                if (d.equals("1")) {
                    checkbox4.setChecked(true); // Set the checkbox checked if condition is met
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkbox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.child("0").setValue("1");

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
                databaseReference.addListenerForSingleValueEvent(valueEventListener);

            }
        });
        checkbox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.child("1").setValue("1");
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
                databaseReference.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        checkbox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.child("2").setValue("1");
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
                databaseReference.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        checkbox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dr.child("3").setValue("1");
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
                databaseReference.addListenerForSingleValueEvent(valueEventListener);
            }
        });
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
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move video to another time frame when r1 is clicked
                seekToPosition(0); // Move to the beginning of the video
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move video to another time frame when r2 is clicked
                seekToPosition(60000); // Move to 1 minute (60000 milliseconds) in the video
            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move video to another time frame when r3 is clicked
                seekToPosition(120000); // Move to 2 minutes (120000 milliseconds) in the video
            }
        });

        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Move video to another time frame when r4 is clicked
                seekToPosition(180000); // Move to 3 minutes (180000 milliseconds) in the video
            }
        });

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
    private void seekToPosition(int positionInMillis) {
        if (videoView != null) {
            videoView.seekTo(positionInMillis);
        }
    }
}
