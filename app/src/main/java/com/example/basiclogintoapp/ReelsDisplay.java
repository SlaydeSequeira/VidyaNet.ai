package com.example.basiclogintoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basiclogintoapp.Model.Video;
import com.example.basiclogintoapp.adapter.VideoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ReelsDisplay extends AppCompatActivity {

    RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    List<Video> videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reels_display);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a list to hold Video objects
        videoList = new ArrayList<>();

        // Get a reference to the "videos" node in the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference videosRef = database.getReference("videos");

        // Read data from the database
        videosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the list before populating with new data
                videoList.clear();

                // Loop through all children under "videos"
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String UID = snapshot.child("UID").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String title = snapshot.child("title").getValue(String.class);
                    String url = snapshot.child("url").getValue(String.class);

                    // Add new Video to the list
                    videoList.add(new Video(UID, description, title, url));
                }

                // Set or update the adapter with new data
                if (videoAdapter == null) {
                    videoAdapter = new VideoAdapter(videoList);
                    recyclerView.setAdapter(videoAdapter);
                } else {
                    videoAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
                System.err.println("Error: " + databaseError.getMessage());
            }
        });
    }
}
