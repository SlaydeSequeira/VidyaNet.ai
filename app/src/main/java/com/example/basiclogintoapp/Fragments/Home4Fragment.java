package com.example.basiclogintoapp.Fragments;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basiclogintoapp.HomePage;
import com.example.basiclogintoapp.R;
import com.example.basiclogintoapp.adapter.DataAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class Home4Fragment extends Fragment {
    private String[] data1 = new String[100];
    private String[] data2 = new String[100];
    private String[] data3 = new String[100];
    private String[] data4 = new String[100];

    private int count = 0; // Counter variable

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home4, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Clear previous items
        clearData();

        // Fetch data from Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("MyUsers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey(); // Get uid
                    String username = snapshot.child("username").getValue(String.class); // Get username
                    String url = snapshot.child("imageURL").getValue(String.class); // Get imageURL
                    String pts = snapshot.child("points").getValue(String.class);
                    if (uid != null && username != null) {
                        data1[count] = uid;
                        data2[count] = username;
                        data3[count]= url;
                        data4[count]= pts;
                        count++;
                    }
                }

                // Find the index with the highest points
                int highestPointsIndex = 0;
                int highestPoints = Integer.MIN_VALUE;
                for (int i = 0; i < count; i++) {
                    int currentPoints = Integer.parseInt(data4[i]);
                    if (currentPoints > highestPoints) {
                        highestPoints = currentPoints;
                        highestPointsIndex = i;
                    }
                }

                // Get the username with the highest points
                String highestPointsUsername = data2[highestPointsIndex];
                String highestPointsString = data4[highestPointsIndex];

                // Show notification with the highest points and corresponding username
                showNotification(highestPointsUsername, highestPointsString);

                // Create an adapter for the dataset and set it to RecyclerView
                String[] uids = Arrays.copyOf(data1, count);
                String[] usernames = Arrays.copyOf(data2, count);
                String[] imageURLs = Arrays.copyOf(data3, count);
                String[] points = Arrays.copyOf(data4, count);
                recyclerView.setAdapter(new DataAdapter(getActivity(), usernames, imageURLs, points, uids));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        return view;
    }

    // Method to clear previous data
    private void clearData() {
        Arrays.fill(data1, null);
        Arrays.fill(data2, null);
        Arrays.fill(data3, null);
        Arrays.fill(data4, null);
        count = 0;
    }
    private void showNotification(String username,String points) {
        // Create a notification channel
        String channelId = "channel_id";
        CharSequence channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }

        // Create an intent to open the app when the notification is clicked
        Intent intent = new Intent(getContext(), HomePage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getContext(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Create the notification
        Notification notification = new Notification.Builder(getContext(), channelId)
                .setContentTitle("Leaderboard Stats")
                .setContentText(username +" is at the top of the leaderboard with "+points+" points")
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
